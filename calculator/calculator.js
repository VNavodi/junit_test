/* =============================================
   CALCULATOR — Full-Featured JavaScript Engine
   Features:
    • Standard arithmetic  (+, −, ×, ÷, %)
    • Scientific functions  (sin, cos, tan, log, ln, √, x², x³, xʸ, 1/x, n!, |x|)
    • Constants  (π, e)
    • Memory operations  (MC, MR, M+, M−)
    • Parentheses balancing
    • Calculation history (click to restore)
    • Keyboard support
    • DEG / RAD mode
    • Chained operations
    • Error handling
   ============================================= */

'use strict';

// ── State ──────────────────────────────────────────────────────────────────
const state = {
  current:            '0',     // what shows on the display
  previous:           '',      // left operand stored
  operator:           null,    // pending operator
  justEvaluated:      false,   // did we just press "="?
  waitingForOperand:  false,   // did we just press an operator? clear display on next digit
  expression:         '',      // history display string
  memory:             0,       // M register
  angleDeg:           true,    // DEG = true, RAD = false
  parenDepth:         0,       // how many open parens
  history:            [],      // [{expr, result}]
  historyOpen:        false,
  mode:               'standard',
};

// ── DOM refs ───────────────────────────────────────────────────────────────
const mainDisplay    = document.getElementById('main-display');
const historyDisplay = document.getElementById('history-display');
const sciPanel       = document.getElementById('sci-panel');
const btnGrid        = document.getElementById('btn-grid');
const calculator     = document.getElementById('calculator');
const historyPanel   = document.getElementById('history-panel');
const historyList    = document.getElementById('history-list');
const toastEl        = document.getElementById('toast');
const btnDeg         = document.getElementById('btn-deg');

// ── Helpers ────────────────────────────────────────────────────────────────
function updateDisplay() {
  const txt = state.current;
  mainDisplay.classList.remove('shrink', 'error');
  if (txt.length > 12)      mainDisplay.classList.add('shrink');
  if (txt === 'Error' || txt === 'Undefined' || txt.includes('NaN'))
    mainDisplay.classList.add('error');
  mainDisplay.textContent = txt;
  historyDisplay.textContent = state.expression;
}

function toNum(s) {
  return parseFloat(s.replace(/,/g, ''));
}

function fmtNum(n) {
  if (!isFinite(n)) return 'Error';
  if (isNaN(n))    return 'Error';
  // Limit to 12 significant digits to avoid floating-point noise
  const s = parseFloat(n.toPrecision(12)).toString();
  return s;
}

let toastTimer;
function showToast(msg) {
  clearTimeout(toastTimer);
  toastEl.textContent = msg;
  toastEl.classList.add('show');
  toastTimer = setTimeout(() => toastEl.classList.remove('show'), 1800);
}

function highlightOp(op) {
  document.querySelectorAll('.btn-op').forEach(b => b.classList.remove('active-op'));
  if (op) {
    const map = { '+': 'btn-add', '−': 'btn-sub', '×': 'btn-mul', '÷': 'btn-div', '^': null };
    const id  = map[op];
    if (id) document.getElementById(id)?.classList.add('active-op');
  }
}

function factorial(n) {
  n = Math.floor(n);
  if (n < 0)  return NaN;
  if (n > 170) return Infinity;
  let r = 1;
  for (let i = 2; i <= n; i++) r *= i;
  return r;
}

function toRad(deg) { return deg * (Math.PI / 180); }

// ── Core operations ────────────────────────────────────────────────────────
function applyOperator(prev, op, curr) {
  const a = toNum(prev);
  const b = toNum(curr);
  switch (op) {
    case '+': return a + b;
    case '−': return a - b;
    case '×': return a * b;
    case '÷': return b === 0 ? NaN : a / b;
    case '^': return Math.pow(a, b);
    default:  return b;
  }
}

// ── Input handlers ─────────────────────────────────────────────────────────
function inputDigit(d) {
  if (state.justEvaluated) {
    // After "=" — start a brand-new calculation
    state.current           = d;
    state.expression        = '';
    state.justEvaluated     = false;
    state.waitingForOperand = false;
  } else if (state.waitingForOperand) {
    // After an operator — replace the display with the new digit
    state.current           = d;
    state.waitingForOperand = false;
  } else {
    state.current = state.current === '0' ? d : state.current + d;
  }
  updateDisplay();
}

function inputDot() {
  if (state.justEvaluated) {
    state.current           = '0.';
    state.justEvaluated     = false;
    state.waitingForOperand = false;
    state.expression        = '';
  } else if (state.waitingForOperand) {
    // After an operator — start a fresh decimal number
    state.current           = '0.';
    state.waitingForOperand = false;
  } else if (!state.current.includes('.')) {
    state.current += '.';
  }
  updateDisplay();
}

function inputOp(op) {
  highlightOp(op);

  if (state.operator && !state.justEvaluated && !state.waitingForOperand) {
    // Chain: compute running total (e.g. 5 + 3 × → computes 5+3 first)
    const result    = applyOperator(state.previous, state.operator, state.current);
    state.previous  = fmtNum(result);
    state.current   = fmtNum(result);
  } else {
    state.previous = state.current;
  }

  state.operator          = op;
  state.justEvaluated     = false;
  state.waitingForOperand = true;   // ← next digit replaces the display
  state.expression        = `${state.previous} ${op}`;
  updateDisplay();
}

function calculate() {
  if (!state.operator || state.previous === '') return;

  const expr   = `${state.previous} ${state.operator} ${state.current}`;
  const result = applyOperator(state.previous, state.operator, state.current);
  const resStr = isNaN(result) ? 'Error' : fmtNum(result);

  if (resStr !== 'Error') {
    addHistory(expr + ' =', resStr);
  }

  state.expression    = expr + ' =';
  state.current       = resStr;
  state.previous      = '';
  state.operator      = null;
  state.justEvaluated = true;

  highlightOp(null);
  updateDisplay();
}

function clearAll() {
  state.current           = '0';
  state.previous          = '';
  state.operator          = null;
  state.justEvaluated     = false;
  state.waitingForOperand = false;
  state.expression        = '';
  state.parenDepth        = 0;
  highlightOp(null);
  updateDisplay();
}

function toggleSign() {
  if (state.current === '0' || state.current === 'Error') return;
  state.current = state.current.startsWith('-')
    ? state.current.slice(1)
    : '-' + state.current;
  updateDisplay();
}

function percent() {
  const n = toNum(state.current);
  if (state.previous && state.operator) {
    state.current = fmtNum(toNum(state.previous) * n / 100);
  } else {
    state.current = fmtNum(n / 100);
  }
  updateDisplay();
}

// ── Scientific ─────────────────────────────────────────────────────────────
function sciOp(fn) {
  const n   = toNum(state.current);
  const deg = state.angleDeg;
  let result;
  switch (fn) {
    case 'sin':  result = Math.sin(deg ? toRad(n) : n); break;
    case 'cos':  result = Math.cos(deg ? toRad(n) : n); break;
    case 'tan':  result = Math.tan(deg ? toRad(n) : n); break;
    case 'log':  result = n > 0 ? Math.log10(n) : NaN;  break;
    case 'ln':   result = n > 0 ? Math.log(n)   : NaN;  break;
    case 'sqrt': result = n >= 0 ? Math.sqrt(n)  : NaN;  break;
    case 'pow2': result = Math.pow(n, 2); break;
    case 'pow3': result = Math.pow(n, 3); break;
    case 'inv':  result = n !== 0 ? 1 / n : NaN; break;
    case 'fact': result = factorial(n); break;
    case 'abs':  result = Math.abs(n); break;
    default:     result = n;
  }

  const label = {
    sin:'sin', cos:'cos', tan:'tan', log:'log', ln:'ln',
    sqrt:'√', pow2:'²', pow3:'³', inv:'1/', fact:'!', abs:'|·|'
  }[fn] || fn;

  const resStr = isNaN(result) || !isFinite(result) ? 'Error' : fmtNum(result);
  if (resStr !== 'Error') {
    addHistory(`${fn}(${n})`, resStr);
  }
  state.expression    = `${fn}(${state.current}) =`;
  state.current       = resStr;
  state.justEvaluated = true;
  updateDisplay();
}

function inputConst(c) {
  const val = c === 'π' ? Math.PI.toString() : Math.E.toString();
  if (state.justEvaluated) {
    state.current      = val;
    state.expression   = '';
    state.justEvaluated = false;
  } else {
    state.current = (state.current === '0') ? val : state.current + val;
  }
  updateDisplay();
}

function inputParen() {
  // Smart: if current is not 0 and parenDepth > 0, close; else open
  if (state.parenDepth > 0 && state.current !== '0') {
    state.current   += ')';
    state.parenDepth--;
  } else {
    state.current   = state.current === '0' ? '(' : state.current + '(';
    state.parenDepth++;
  }
  updateDisplay();
}

function toggleAngle() {
  state.angleDeg = !state.angleDeg;
  btnDeg.textContent = state.angleDeg ? 'DEG' : 'RAD';
  showToast(state.angleDeg ? 'Degrees mode' : 'Radians mode');
}

// ── Memory ─────────────────────────────────────────────────────────────────
function memOp(op) {
  const n = toNum(state.current);
  switch (op) {
    case 'MC': state.memory = 0;              showToast('Memory cleared'); break;
    case 'MR':
      state.current       = fmtNum(state.memory);
      state.justEvaluated = true;
      showToast(`Memory: ${state.memory}`);
      updateDisplay();
      return;
    case 'M+': state.memory += n;             showToast(`M+ → ${fmtNum(state.memory)}`); break;
    case 'M-': state.memory -= n;             showToast(`M− → ${fmtNum(state.memory)}`); break;
  }
}

// ── History ────────────────────────────────────────────────────────────────
function addHistory(expr, result) {
  state.history.unshift({ expr, result });
  if (state.history.length > 50) state.history.pop();
  renderHistory();
}

function renderHistory() {
  historyList.innerHTML = '';
  if (state.history.length === 0) {
    historyList.innerHTML = '<li style="color:var(--text-dim);font-size:12px;text-align:center;padding:12px;">No history yet</li>';
    return;
  }
  state.history.forEach(({ expr, result }, i) => {
    const li = document.createElement('li');
    li.className = 'history-item';
    li.innerHTML = `<span class="history-expr">${expr}</span><span class="history-res">${result}</span>`;
    li.onclick = () => {
      state.current       = result;
      state.justEvaluated = true;
      state.expression    = expr;
      updateDisplay();
      showToast('Restored from history');
    };
    historyList.appendChild(li);
  });
}

function clearHistory() {
  state.history = [];
  renderHistory();
  showToast('History cleared');
}

function toggleHistory() {
  state.historyOpen = !state.historyOpen;
  historyPanel.classList.toggle('open', state.historyOpen);
  renderHistory();
}

// ── Mode toggle ────────────────────────────────────────────────────────────
function setMode(mode) {
  state.mode = mode;
  document.getElementById('btn-standard').classList.toggle('active', mode === 'standard');
  document.getElementById('btn-scientific').classList.toggle('active', mode === 'scientific');
  sciPanel.classList.toggle('visible', mode === 'scientific');
  calculator.classList.toggle('sci-mode', mode === 'scientific');
}

// ── Keyboard support ───────────────────────────────────────────────────────
document.addEventListener('keydown', e => {
  const k = e.key;
  if (k >= '0' && k <= '9')          { inputDigit(k); pulse(`btn-${k}`); }
  else if (k === '.')                 { inputDot(); pulse('btn-dot'); }
  else if (k === '+')                 { inputOp('+'); pulse('btn-add'); }
  else if (k === '-')                 { inputOp('−'); pulse('btn-sub'); }
  else if (k === '*')                 { inputOp('×'); pulse('btn-mul'); }
  else if (k === '/')                 { e.preventDefault(); inputOp('÷'); pulse('btn-div'); }
  else if (k === '^')                 { inputOp('^'); }
  else if (k === 'Enter' || k === '='){ calculate(); pulse('btn-eq'); }
  else if (k === 'Backspace')         { backspace(); }
  else if (k === 'Escape')            { clearAll(); pulse('btn-ac'); }
  else if (k === '%')                 { percent(); pulse('btn-pct'); }
});

function pulse(id) {
  const el = document.getElementById(id);
  if (!el) return;
  el.style.transform = 'scale(0.92)';
  setTimeout(() => (el.style.transform = ''), 120);
}

function backspace() {
  if (state.justEvaluated || state.current === 'Error') {
    clearAll(); return;
  }
  state.current = state.current.length > 1
    ? state.current.slice(0, -1)
    : '0';
  updateDisplay();
}

// ── Keyboard hint ──────────────────────────────────────────────────────────
const hint = document.createElement('div');
hint.className = 'kb-hint';
hint.innerHTML = '<kbd>⌨</kbd> keyboard supported';
document.body.appendChild(hint);

// ── Boot ───────────────────────────────────────────────────────────────────
updateDisplay();
renderHistory();
