const API_BASE = 'http://localhost:8080'; // порт API-Gateway

async function fetchInventory() {
  const res = await fetch(`${API_BASE}/inventory`);
  return res.json();
}

async function createBooking(itemId, userId = 'user1') {
  const res = await fetch(`${API_BASE}/bookings`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ itemId, userId })
  });
  return res.json();
}

function renderInventory(items) {
  const app = document.getElementById('app');
  app.innerHTML = ''; 
  items.forEach(item => {
    const row = document.createElement('div');
    row.textContent = `${item.type}: ${item.availableCount} шт за ${item.price}₴ `;
    const btn = document.createElement('button');
    btn.textContent = 'Забронювати';
    btn.onclick = async () => {
      const booking = await createBooking(item.id);
      alert(`Бронювання створено: ${booking.id}`);
      init(); // поновити список
    };
    row.appendChild(btn);
    app.appendChild(row);
  });
}

async function init() {
  try {
    const inv = await fetchInventory();
    renderInventory(inv);
  } catch (e) {
    document.getElementById('app').textContent = 'Не вдалося завантажити дані';
    console.error(e);
  }
}

init();
