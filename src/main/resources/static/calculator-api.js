'use strict';

/**
 * calculator-api.js
 * Same UI as calculator.js, but all math is computed by the Java REST API
 * via POST /api/calculate  →  Calculator.java on the backend.
 *
 * Flow: Button click → fetch('/api/calculate') → Java Calculator.java → result → display
 */

// ── State ──────────────────────────────────────────────────────────────────
const state = {
  current:           '0',
  previous:          '',
  operator:          null,
  justEvaluated:     false,
  waitingForOperand: false,
  expression:        '',
  memory:            0,
  angleDeg:          true,
  parenDepth:        0,
  history:           [],
  historyOpen:       false,
  mode:              'standard',
};

// ── DOM refs ───────────────────────────────────────────────────────────────
const mainDisplay    = document.getElementById('main-display');
const historyDisplay = document.getElementById('history-display');
const sciPanel       = document.getElementById('sci-panel');
const calculator     = document.getElementById('calculator');
const historyPanel   = document.getElementById('history-panel');
const historyList    = document.getElementById('history-list');
const toastEl        = document.getElementById('toast');
const btnDeg         = document.getElementById('btn-deg');
const apiBadge       = document.getElementById('api-badge');

// ── REST API helper ────────────────────────────────────────────────────────
async function callApi(payload) {
  try {
    const res  = await fetch('/api/calculate', {
      method:  'POST',
      headers: { 'Content-Type': 'application/json' },
      body:    JSON.stringify(payload),
    });
    const data = await res.json();
    if (data.error) return 'Error';
    const val = data.result;
    // toPrecision to avoid floating point noise
    if (typeof val === 'number') {
      return parseFloat(val.toPrecision(12)).toString();
    }
    return String(val);
  } catch (e) {
    apiBadge.textContent = '⚠️ API offline';
    apiBadge.classList.add('error');
    return 'Error';
  }
}

// ── Display ────────────────────────────────────────────────────────────────
function updateDisplay() {
  const txt = state.current;
  mainDisplay.classList.remove('shrink', 'error');
  if (txt.length > 12)    mainDisplay.classList.add('shrink');
  if (txt === 'Error')    mainDisplay.classList.add('error');
  mainDisplay.textContent = txt;
  historyDisplay.textContent = state.expression;
}

function fmtNum(n) {
  const v = parseFloat(n);
  if (isNaN(v) || !isFinite(v)) return 'Error';
  return parseFloat(v.toPrecision(12)).toString();
}
function toNum(s) { return parseFloat(String(s).replace(/,/g, '')); }

let toastTimer;
function showToast(msg) {
  clearTimeout(toastTimer);
  toastEl.textContent = msg;
  toastEl.classList.add('show');
  toastTimer = setTimeout(() => toastEl.classList.remove('show'), 1800);
}

function highlightOp(op) {
  document.querySelectorAll('.btn-op').forEach(b => b.classList.remove('active-op'));
  const map = { '+':'btn-add', '−':'btn-sub', '×':'btn-mul', '÷':'btn-div' };
  if (op && map[op]) document.getElementById(map[op])?.classList.add('active-op');
}

// ── Digit / Dot input (no API needed) ─────────────────────────────────────
function inputDigit(d) {
  if (state.justEvaluated) {
    state.current = d; state.expression = '';
    state.justEvaluated = false; state.waitingForOperand = false;
  } else if (state.waitingForOperand) {
    state.current = d; state.waitingForOperand = false;
  } else {
    state.current = state.current === '0' ? d : state.current + d;
  }
  updateDisplay();
}

function inputDot() {
  if (state.justEvaluated || state.waitingForOperand) {
    state.current = '0.';
    state.justEvaluated = false; state.waitingForOperand = false;
    state.expression = state.waitingForOperand ? state.expression : '';
  } else if (!state.current.includes('.')) {
    state.current += '.';
  }
  updateDisplay();
}

// ── Operator input (no API — just stores operator) ─────────────────────────
function inputOp(op) {
  highlightOp(op);
  if (state.operator && !state.justEvaluated && !state.waitingForOperand) {
    // chain: compute on next = press — store for now
  } else {
    state.previous = state.current;
  }
  state.operator = op;
  state.justEvaluated = false;
  state.waitingForOperand = true;
  state.expression = `${state.previous} ${op}`;
  updateDisplay();
}

// ── CALCULATE — calls Java via REST API ────────────────────────────────────
async function calculate() {
  if (!state.operator || state.previous === '') return;

  const expr   = `${state.previous} ${state.operator} ${state.current}`;
  const result = await callApi({
    type:     'binary',
    a:        toNum(state.previous),
    operator: state.operator,
    b:        toNum(state.current),
  });

  if (result !== 'Error') addHistory(expr + ' =', result);
  state.expression    = expr + ' =';
  state.current       = result;
  state.previous      = '';
  state.operator      = null;
  state.justEvaluated = true;
  state.waitingForOperand = false;
  highlightOp(null);
  updateDisplay();
}

// ── SCIENTIFIC OPS — calls Java via REST API ───────────────────────────────
async function sciOp(fn) {
  const value  = toNum(state.current);
  const result = await callApi({
    type:     'unary',
    function: fn,
    value:    value,
    angleDeg: state.angleDeg,
  });

  if (result !== 'Error') addHistory(`${fn}(${value})`, result);
  state.expression    = `${fn}(${state.current}) =`;
  state.current       = result;
  state.justEvaluated = true;
  updateDisplay();
}

// ── PERCENT — calls Java via REST API ──────────────────────────────────────
async function percent() {
  const n      = toNum(state.current);
  const result = await callApi({ type: 'unary', function: 'percent', value: n });
  state.current = result;
  updateDisplay();
}

// ── TOGGLE SIGN — calls Java via REST API ──────────────────────────────────
async function toggleSign() {
  if (state.current === '0' || state.current === 'Error') return;
  const result = await callApi({ type: 'unary', function: 'negate', value: toNum(state.current) });
  state.current = result;
  updateDisplay();
}

// ── CLEAR (no API needed) ─────────────────────────────────────────────────
function clearAll() {
  state.current = '0'; state.previous = ''; state.operator = null;
  state.justEvaluated = false; state.waitingForOperand = false;
  state.expression = ''; state.parenDepth = 0;
  highlightOp(null); updateDisplay();
}

// ── CONSTANTS ─────────────────────────────────────────────────────────────
function inputConst(c) {
  const val = c === 'π' ? Math.PI.toString() : Math.E.toString();
  if (state.justEvaluated || state.waitingForOperand) {
    state.current = val; state.justEvaluated = false; state.waitingForOperand = false;
  } else {
    state.current = state.current === '0' ? val : state.current + val;
  }
  updateDisplay();
}

function inputParen() {
  if (state.parenDepth > 0 && state.current !== '0') {
    state.current += ')'; state.parenDepth--;
  } else {
    state.current = state.current === '0' ? '(' : state.current + '(';
    state.parenDepth++;
  }
  updateDisplay();
}

function toggleAngle() {
  state.angleDeg = !state.angleDeg;
  btnDeg.textContent = state.angleDeg ? 'DEG' : 'RAD';
  showToast(state.angleDeg ? 'Degrees mode' : 'Radians mode');
}

// ── MEMORY (no API needed — stored in JS state) ───────────────────────────
function memOp(op) {
  const n = toNum(state.current);
  switch (op) {
    case 'MC': state.memory = 0;        showToast('Memory cleared'); break;
    case 'MR':
      state.current = fmtNum(state.memory);
      state.justEvaluated = true;
      showToast(`Memory: ${state.memory}`);
      updateDisplay(); return;
    case 'M+': state.memory += n;       showToast(`M+ → ${fmtNum(state.memory)}`); break;
    case 'M-': state.memory -= n;       showToast(`M− → ${fmtNum(state.memory)}`); break;
  }
}

// ── HISTORY ───────────────────────────────────────────────────────────────
function addHistory(expr, result) {
  state.history.unshift({ expr, result });
  if (state.history.length > 50) state.history.pop();
  renderHistory();
}
function renderHistory() {
  historyList.innerHTML = '';
  if (!state.history.length) {
    historyList.innerHTML = '<li style="color:var(--text-dim);font-size:12px;text-align:center;padding:12px">No history yet</li>';
    return;
  }
  state.history.forEach(({ expr, result }) => {
    const li = document.createElement('li');
    li.className = 'history-item';
    li.innerHTML = `<span class="history-expr">${expr}</span><span class="history-res">${result}</span>`;
    li.onclick = () => {
      state.current = result; state.justEvaluated = true; state.expression = expr;
      updateDisplay(); showToast('Restored from history');
    };
    historyList.appendChild(li);
  });
}
function clearHistory()  { state.history = []; renderHistory(); showToast('History cleared'); }
function toggleHistory() {
  state.historyOpen = !state.historyOpen;
  historyPanel.classList.toggle('open', state.historyOpen);
  renderHistory();
}

// ── MODE ──────────────────────────────────────────────────────────────────
function setMode(mode) {
  state.mode = mode;
  document.getElementById('btn-standard').classList.toggle('active', mode === 'standard');
  document.getElementById('btn-scientific').classList.toggle('active', mode === 'scientific');
  sciPanel.classList.toggle('visible', mode === 'scientific');
  calculator.classList.toggle('sci-mode', mode === 'scientific');
}

// ── KEYBOARD ──────────────────────────────────────────────────────────────
document.addEventListener('keydown', e => {
  if (document.activeElement?.classList.contains('btn') &&
      (e.key === 'Enter' || e.key === ' ')) {
    e.preventDefault(); document.activeElement.blur(); return;
  }
  const k = e.key;
  if      (k >= '0' && k <= '9')            { e.preventDefault(); inputDigit(k); pulse(`btn-${k}`); }
  else if (k === '.')                         { e.preventDefault(); inputDot(); pulse('btn-dot'); }
  else if (k === '+')                         { e.preventDefault(); inputOp('+'); pulse('btn-add'); }
  else if (k === '-')                         { e.preventDefault(); inputOp('−'); pulse('btn-sub'); }
  else if (k === '*')                         { e.preventDefault(); inputOp('×'); pulse('btn-mul'); }
  else if (k === '/')                         { e.preventDefault(); inputOp('÷'); pulse('btn-div'); }
  else if (k === '^')                         { e.preventDefault(); inputOp('^'); }
  else if (k === 'Enter' || k === '=')        { e.preventDefault(); calculate(); pulse('btn-eq'); }
  else if (k === 'Backspace' || k === 'Delete'){ e.preventDefault(); backspace(); }
  else if (k === 'Escape' || k === 'c' || k === 'C') { e.preventDefault(); clearAll(); pulse('btn-ac'); }
  else if (k === '%')                         { e.preventDefault(); percent(); }
  else if (k === 'F9')                        { e.preventDefault(); toggleSign(); }
});

function pulse(id) {
  const el = document.getElementById(id);
  if (!el) return;
  el.style.transform = 'scale(0.92)';
  setTimeout(() => el.style.transform = '', 120);
}
function backspace() {
  if (state.justEvaluated || state.current === 'Error') { clearAll(); return; }
  state.current = state.current.length > 1 ? state.current.slice(0, -1) : '0';
  updateDisplay();
}
document.addEventListener('mouseup', e => {
  if (e.target?.classList.contains('btn') || e.target?.classList.contains('mode-btn'))
    setTimeout(() => e.target.blur(), 50);
});

// ── Boot ──────────────────────────────────────────────────────────────────
async function checkApiHealth() {
  try {
    const res  = await fetch('/api/health');
    const data = await res.json();
    if (data.status === 'UP') {
      apiBadge.textContent = '⚡ Java API ✓';
      apiBadge.classList.remove('error');
    }
  } catch {
    apiBadge.textContent = '⚠️ API offline';
    apiBadge.classList.add('error');
  }
}

updateDisplay();
renderHistory();
checkApiHealth();
window.focus();
