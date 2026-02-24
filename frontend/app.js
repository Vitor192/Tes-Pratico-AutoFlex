const defaultBase = "http://localhost:8080";
const storedBase = localStorage.getItem("apiBaseUrl");
let API_BASE = storedBase || defaultBase;

const apiBaseInput = document.getElementById("apiBaseUrl");
apiBaseInput.value = API_BASE;
document.getElementById("saveBaseUrl").addEventListener("click", () => {
  API_BASE = apiBaseInput.value || defaultBase;
  localStorage.setItem("apiBaseUrl", API_BASE);
  loadAll();
});

function navInit() {
  const links = document.querySelectorAll(".nav-link[data-section]");
  links.forEach(l => {
    l.addEventListener("click", (e) => {
      e.preventDefault();
      links.forEach(x => x.classList.remove("active"));
      l.classList.add("active");
      const sec = l.getAttribute("data-section");
      document.querySelectorAll(".section").forEach(s => s.classList.add("d-none"));
      document.getElementById(`section-${sec}`).classList.remove("d-none");
    });
  });
}

async function fetchJson(path, options = {}) {
  const res = await fetch(`${API_BASE}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options
  });
  if (!res.ok) {
    const text = await res.text();
    throw new Error(`${res.status} ${res.statusText}: ${text}`);
  }
  if (res.status === 204) return null;
  return res.json();
}

// Products
async function loadProducts() {
  const tbody = document.getElementById("productsTableBody");
  tbody.innerHTML = "";
  const products = await fetchJson("/products");
  products.forEach(p => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${p.id}</td>
      <td><input class="form-control form-control-sm" value="${p.name}" data-field="name"/></td>
      <td><input class="form-control form-control-sm" type="number" step="0.01" value="${p.price}" data-field="price"/></td>
      <td class="d-flex gap-2">
        <button class="btn btn-sm btn-outline-primary">Save</button>
        <button class="btn btn-sm btn-outline-danger">Delete</button>
      </td>`;
    const [saveBtn, delBtn] = tr.querySelectorAll("button");
    saveBtn.addEventListener("click", async () => {
      const name = tr.querySelector('input[data-field="name"]').value;
      const price = parseFloat(tr.querySelector('input[data-field="price"]').value);
      await fetchJson(`/products/${p.id}`, { method: "PUT", body: JSON.stringify({ name, price })});
      await loadProducts();
      await loadAssociationSelectors();
    });
    delBtn.addEventListener("click", async () => {
      await fetchJson(`/products/${p.id}`, { method: "DELETE" });
      await loadProducts();
      await loadAssociationSelectors();
    });
    tbody.appendChild(tr);
  });
}

document.getElementById("productForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const name = document.getElementById("productName").value.trim();
  const price = parseFloat(document.getElementById("productPrice").value);
  await fetchJson("/products", { method: "POST", body: JSON.stringify({ name, price })});
  e.target.reset();
  await loadProducts();
  await loadAssociationSelectors();
});

// Raw Materials
async function loadRawMaterials() {
  const tbody = document.getElementById("rawMaterialsTableBody");
  tbody.innerHTML = "";
  const rms = await fetchJson("/raw-materials");
  rms.forEach(rm => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${rm.id}</td>
      <td><input class="form-control form-control-sm" value="${rm.name}" data-field="name"/></td>
      <td><input class="form-control form-control-sm" type="number" min="0" step="1" value="${rm.stockQuantity}" data-field="stock"/></td>
      <td class="d-flex gap-2">
        <button class="btn btn-sm btn-outline-primary">Save</button>
        <button class="btn btn-sm btn-outline-danger">Delete</button>
      </td>`;
    const [saveBtn, delBtn] = tr.querySelectorAll("button");
    saveBtn.addEventListener("click", async () => {
      const name = tr.querySelector('input[data-field="name"]').value;
      const stockQuantity = parseInt(tr.querySelector('input[data-field="stock"]').value, 10);
      await fetchJson(`/raw-materials/${rm.id}`, { method: "PUT", body: JSON.stringify({ name, stockQuantity })});
      await loadRawMaterials();
      await loadAssociationSelectors();
    });
    delBtn.addEventListener("click", async () => {
      await fetchJson(`/raw-materials/${rm.id}`, { method: "DELETE" });
      await loadRawMaterials();
      await loadAssociationSelectors();
    });
    tbody.appendChild(tr);
  });
}

document.getElementById("rawMaterialForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const name = document.getElementById("rawMaterialName").value.trim();
  const stockQuantity = parseInt(document.getElementById("rawMaterialStock").value, 10);
  await fetchJson("/raw-materials", { method: "POST", body: JSON.stringify({ name, stockQuantity })});
  e.target.reset();
  await loadRawMaterials();
  await loadAssociationSelectors();
});

// Associations
async function loadAssociationSelectors() {
  const pSel = document.getElementById("associationProduct");
  const rSel = document.getElementById("associationRawMaterial");
  const [products, rms] = await Promise.all([fetchJson("/products"), fetchJson("/raw-materials")]);
  pSel.innerHTML = products.map(p => `<option value="${p.id}">${p.name} (#${p.id})</option>`).join("");
  rSel.innerHTML = rms.map(r => `<option value="${r.id}">${r.name} (#${r.id})</option>`).join("");
}

document.getElementById("associationForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const productId = parseInt(document.getElementById("associationProduct").value, 10);
  const rawMaterialId = parseInt(document.getElementById("associationRawMaterial").value, 10);
  const quantityRequired = parseInt(document.getElementById("associationQuantity").value, 10);
  await fetchJson("/product-raw-materials", { method: "POST", body: JSON.stringify({ productId, rawMaterialId, quantityRequired })});
  e.target.reset();
  alert("Association created.");
});

document.getElementById("deleteAssociationBtn").addEventListener("click", async () => {
  const id = parseInt(document.getElementById("deleteAssociationId").value, 10);
  if (!id) return;
  await fetchJson(`/product-raw-materials/${id}`, { method: "DELETE" });
  alert("Association deleted if it existed.");
});

// Production
async function loadProduction() {
  const tbody = document.getElementById("productionTableBody");
  tbody.innerHTML = "";
  const list = await fetchJson("/production/suggestions");
  list.forEach(item => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${item.productId}</td>
      <td>${item.productName}</td>
      <td>${item.quantityToProduce}</td>
      <td>${item.totalValue}</td>`;
    tbody.appendChild(tr);
  });
}
document.getElementById("refreshSuggestions").addEventListener("click", async () => {
  await loadProduction();
});

async function loadAll() {
  await Promise.all([loadProducts(), loadRawMaterials(), loadAssociationSelectors(), loadProduction()]);
}

navInit();
loadAll().catch(err => console.error(err));
