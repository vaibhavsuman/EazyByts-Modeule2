// Global variables
let currentUser = null;
let isLoginMode = true;

// Global test functions for debugging
window.testLogin = function() {
    console.log('Test login function called');
    showAuthModal('login');
};

window.testLoginDirect = function() {
    console.log('Testing login directly with credentials');
    document.getElementById('username').value = 'Vaibhav';
    document.getElementById('password').value = 'Vaibhav@123';
    handleAuth(null);
};

window.testAuthForm = function() {
    console.log('Testing auth form submission');
    const form = document.getElementById('authForm');
    if (form) {
        console.log('Form found, submitting...');
        form.dispatchEvent(new Event('submit'));
    } else {
        console.log('Form not found');
    }
};

// DOM Content Loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM Content Loaded - Initializing app');
    initializeApp();
    setupEventListeners();
    loadStocks();
});

// Initialize Application
function initializeApp() {
    // Check if user is logged in
    checkAuthStatus();
    
    // Initialize navigation
    setupNavigation();
    
    // Setup responsive menu
    setupResponsiveMenu();
}

// Event Listeners
function setupEventListeners() {
    // Auth form
    const authForm = document.getElementById('authForm');
    if (authForm) {
        authForm.addEventListener('submit', handleAuth);
        console.log('Auth form event listener attached');
        
        // Also add click listener to submit button as backup
        const submitButton = authForm.querySelector('button[type="submit"]');
        if (submitButton) {
            submitButton.addEventListener('click', function(e) {
                console.log('Submit button clicked');
                e.preventDefault();
                handleAuth(e);
            });
            console.log('Submit button click listener attached');
        }
    } else {
        console.error('Auth form not found!');
    }
    
    // Trading form
    const tradingForm = document.getElementById('tradingForm');
    if (tradingForm) {
        tradingForm.addEventListener('submit', handleTrading);
    }
    
    // Stock search
    const stockSearch = document.getElementById('stockSearch');
    if (stockSearch) {
        stockSearch.addEventListener('input', debounce(searchStocks, 300));
    }
    
    // Stock symbol input for trading
    const stockSymbol = document.getElementById('stockSymbol');
    if (stockSymbol) {
        stockSymbol.addEventListener('blur', loadStockInfo);
    }
}

// Navigation
function setupNavigation() {
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const section = this.getAttribute('data-section');
            showSection(section);
        });
    });
}

function setupResponsiveMenu() {
    const hamburger = document.getElementById('hamburger');
    const navMenu = document.getElementById('navMenu');
    
    hamburger.addEventListener('click', function() {
        navMenu.classList.toggle('active');
    });
}

function showSection(sectionName) {
    // Hide all sections
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    // Show selected section
    document.getElementById(sectionName).classList.add('active');
    
    // Update navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    document.querySelector(`[data-section="${sectionName}"]`).classList.add('active');
    
    // Close mobile menu
    document.getElementById('navMenu').classList.remove('active');
    
    // Load section-specific data
    switch(sectionName) {
        case 'dashboard':
            loadDashboard();
            break;
        case 'portfolio':
            loadPortfolio();
            break;
        case 'trading':
            // Trading form is already loaded
            break;
        case 'stocks':
            loadStocks();
            break;
    }
}

// Authentication
function checkAuthStatus() {
    // Check if user is already logged in
    const token = localStorage.getItem('authToken');
    if (token) {
        // Check current user status
        fetch('/api/auth/user', { credentials: 'include' })
        .then(response => response.json())
        .then(data => {
            if (data.userId) {
                currentUser = data;
                updateAuthUI(true);
                loadDashboard();
            } else {
                localStorage.removeItem('authToken');
                localStorage.removeItem('userId');
                updateAuthUI(false);
            }
        })
        .catch(() => {
            localStorage.removeItem('authToken');
            localStorage.removeItem('userId');
            updateAuthUI(false);
        });
    } else {
        updateAuthUI(false);
    }
}

function updateAuthUI(isLoggedIn) {
    const authButtons = document.getElementById('authButtons');
    const userInfo = document.getElementById('userInfo');
    const userWelcome = document.getElementById('userWelcome');
    
    if (isLoggedIn) {
        authButtons.style.display = 'none';
        userInfo.style.display = 'flex';
        userWelcome.textContent = `Welcome, ${currentUser.username}!`;
    } else {
        authButtons.style.display = 'flex';
        userInfo.style.display = 'none';
        currentUser = null;
    }
}

function showAuthModal(mode) {
    console.log('showAuthModal called with mode:', mode);
    isLoginMode = mode === 'login';
    const modal = document.getElementById('authModal');
    const title = document.getElementById('modalTitle');
    const submitText = document.getElementById('submitText');
    const switchText = document.getElementById('switchText');
    const switchLink = document.getElementById('switchLink');
    const registerFields = document.getElementById('registerFields');
    
    console.log('Modal element found:', modal);
    console.log('Title element found:', title);
    console.log('Submit text element found:', submitText);
    console.log('Showing auth modal, mode:', mode, 'isLoginMode:', isLoginMode);
    
    if (isLoginMode) {
        title.textContent = 'Login';
        submitText.textContent = 'Login';
        switchText.textContent = "Don't have an account?";
        switchLink.textContent = 'Register';
        registerFields.style.display = 'none';
    } else {
        title.textContent = 'Register';
        submitText.textContent = 'Register';
        switchText.textContent = 'Already have an account?';
        switchLink.textContent = 'Login';
        registerFields.style.display = 'block';
    }
    
    modal.style.display = 'block';
    console.log('Modal display set to block, current style:', modal.style.display);
}

function closeAuthModal() {
    document.getElementById('authModal').style.display = 'none';
    document.getElementById('authForm').reset();
}

function toggleAuthMode() {
    showAuthModal(isLoginMode ? 'register' : 'login');
}

function handleAuth(e) {
    console.log('handleAuth called with event:', e);
    if (e && e.preventDefault) {
        e.preventDefault();
    }
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;
    
    console.log('Auth attempt:', { username, password: '***', email, isLoginMode });
    console.log('Username field value:', username);
    console.log('Password field value length:', password ? password.length : 0);
    
    if (!username || !password) {
        console.log('Missing username or password');
        showToast('Username and password are required', 'error');
        return;
    }
    
    if (!isLoginMode && !email) {
        console.log('Missing email for registration');
        showToast('Email is required for registration', 'error');
        return;
    }
    
    const endpoint = isLoginMode ? '/api/auth/login' : '/api/auth/register';
    const data = isLoginMode ? { username, password } : { username, password, email };
    
    console.log('Sending request to:', endpoint, data);
    console.log('Request data stringified:', JSON.stringify(data));
    
    if (typeof showLoading === 'function') {
        showLoading(true);
    }
    
    fetch(endpoint, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(data)
    })
    .then(response => {
        console.log('Response status:', response.status);
        return response.json();
    })
    .then(data => {
        console.log('Response data:', data);
        if (data.error) {
            showToast(data.error, 'error');
        } else {
            showToast(data.message, 'success');
            closeAuthModal();
            
            if (isLoginMode) {
                // Store auth token (in a real app, this would be a JWT)
                localStorage.setItem('authToken', data.authToken || 'dummy-token');
                localStorage.setItem('userId', data.userId);
                currentUser = data;
                updateAuthUI(true);
                loadDashboard();
            }
        }
    })
    .catch(error => {
        showToast('An error occurred. Please try again.', 'error');
        console.error('Auth error:', error);
    })
    .finally(() => {
        showLoading(false);
    });
}

function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userId');
    currentUser = null;
    updateAuthUI(false);
    showSection('dashboard');
    showToast('Logged out successfully', 'success');
}

// Dashboard
function loadDashboard() {
    console.log('Loading dashboard, currentUser:', currentUser);
    if (!currentUser) {
        console.log('No current user, cannot load dashboard');
        return;
    }
    
    fetch('/api/portfolio', {
        credentials: 'include' // Include session cookies
    })
    .then(response => {
        console.log('Portfolio response status:', response.status);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('Portfolio data received:', data);
        if (data.error) {
            console.error('Portfolio error:', data.error);
            showToast(data.error, 'error');
        } else {
            updateDashboard(data);
        }
    })
    .catch(error => {
        console.error('Dashboard error:', error);
        showToast('Failed to load dashboard data', 'error');
    });
}

function updateDashboard(portfolio) {
    document.getElementById('totalValue').textContent = formatCurrency(portfolio.totalValue);
    document.getElementById('cashBalance').textContent = formatCurrency(portfolio.cashBalance);
    document.getElementById('investedValue').textContent = formatCurrency(portfolio.investedValue);
    document.getElementById('totalReturn').textContent = formatCurrency(portfolio.totalReturn);
    
    const returnPercent = portfolio.totalReturnPercent || 0;
    const returnElement = document.getElementById('totalReturnPercent');
    returnElement.textContent = `${returnPercent >= 0 ? '+' : ''}${returnPercent.toFixed(2)}%`;
    returnElement.className = `metric-change ${returnPercent >= 0 ? 'positive' : 'negative'}`;
    
    // Update day change if available
    if (portfolio.dayChange !== undefined) {
        const dayChangeElement = document.getElementById('totalChange');
        dayChangeElement.textContent = `${portfolio.dayChange >= 0 ? '+' : ''}${formatCurrency(portfolio.dayChange)} (${portfolio.dayChangePercent || 0}%)`;
        dayChangeElement.className = `metric-change ${portfolio.dayChange >= 0 ? 'positive' : 'negative'}`;
    }
}

// Trading
function handleTrading(e) {
    e.preventDefault();
    
    if (!currentUser) {
        showToast('Please login to trade stocks', 'error');
        return;
    }
    
    const symbol = document.getElementById('stockSymbol').value.toUpperCase();
    const type = document.getElementById('orderType').value;
    const quantity = parseInt(document.getElementById('quantity').value);
    
    if (!symbol || !quantity) {
        showToast('Please fill in all fields', 'error');
        return;
    }
    
    showLoading(true);
    
    fetch(`/api/trading/${type.toLowerCase()}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify({ symbol, quantity })
    })
    .then(response => response.json())
    .then(data => {
        if (data.error) {
            showToast(data.error, 'error');
        } else {
            showToast(data.message, 'success');
            document.getElementById('tradingForm').reset();
            loadDashboard(); // Refresh dashboard
        }
    })
    .catch(error => {
        showToast('Trading failed. Please try again.', 'error');
        console.error('Trading error:', error);
    })
    .finally(() => {
        showLoading(false);
    });
}

function loadStockInfo() {
    const symbol = document.getElementById('stockSymbol').value.toUpperCase();
    if (!symbol) return;
    
    fetch(`/api/stocks/${symbol}`)
    .then(response => response.json())
    .then(data => {
        const stockInfo = document.getElementById('stockInfo');
        if (data.error) {
            stockInfo.innerHTML = '<p style="color: #e74c3c;">Stock not found</p>';
        } else {
            stockInfo.innerHTML = `
                <h4>${data.symbol} - ${data.name}</h4>
                <p><strong>Price:</strong> ${formatCurrency(data.currentPrice)}</p>
                <p><strong>Change:</strong> <span class="${data.dayChange >= 0 ? 'positive' : 'negative'}">${formatCurrency(data.dayChange)} (${data.dayChangePercent || 0}%)</span></p>
                <p><strong>Volume:</strong> ${data.volume ? data.volume.toLocaleString() : 'N/A'}</p>
                <p><strong>Market Cap:</strong> ${data.marketCap ? formatCurrency(data.marketCap) : 'N/A'}</p>
            `;
        }
    })
    .catch(error => {
        console.error('Stock info error:', error);
    });
}

// Portfolio
function loadPortfolio() {
    console.log('Loading portfolio, currentUser:', currentUser);
    if (!currentUser) {
        console.log('No current user, cannot load portfolio');
        return;
    }
    
    Promise.all([
        fetch('/api/portfolio/holdings', { credentials: 'include' }),
        fetch('/api/portfolio/transactions', { credentials: 'include' })
    ])
    .then(responses => {
        responses.forEach((response, index) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status} for ${index === 0 ? 'holdings' : 'transactions'}`);
            }
        });
        return Promise.all(responses.map(r => r.json()));
    })
    .then(([holdings, transactions]) => {
        console.log('Portfolio data loaded:', { holdings, transactions });
        updateHoldingsTable(holdings);
        updateTransactionsTable(transactions);
    })
    .catch(error => {
        console.error('Portfolio error:', error);
        showToast('Failed to load portfolio data', 'error');
    });
}

function updateHoldingsTable(holdings) {
    const tbody = document.querySelector('#holdingsTable tbody');
    
    if (!holdings || holdings.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">No holdings found</td></tr>';
        return;
    }
    
    tbody.innerHTML = holdings.map(holding => `
        <tr>
            <td><strong>${holding.stock.symbol}</strong></td>
            <td>${holding.stock.name}</td>
            <td>${holding.quantity}</td>
            <td>${formatCurrency(holding.averagePrice)}</td>
            <td>${formatCurrency(holding.currentPrice)}</td>
            <td>${formatCurrency(holding.currentValue)}</td>
            <td class="${holding.unrealizedGainLoss >= 0 ? 'positive' : 'negative'}">${formatCurrency(holding.unrealizedGainLoss)}</td>
            <td class="${holding.unrealizedGainLossPercent >= 0 ? 'positive' : 'negative'}">${holding.unrealizedGainLossPercent.toFixed(2)}%</td>
        </tr>
    `).join('');
}

function updateTransactionsTable(transactions) {
    const tbody = document.querySelector('#transactionsTable tbody');
    
    if (!transactions || transactions.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">No transactions found</td></tr>';
        return;
    }
    
    tbody.innerHTML = transactions.map(transaction => `
        <tr>
            <td>${new Date(transaction.createdAt).toLocaleDateString()}</td>
            <td><span class="badge ${transaction.type === 'BUY' ? 'buy' : 'sell'}">${transaction.type}</span></td>
            <td><strong>${transaction.stock.symbol}</strong></td>
            <td>${transaction.quantity}</td>
            <td>${formatCurrency(transaction.pricePerShare)}</td>
            <td>${formatCurrency(transaction.totalAmount)}</td>
            <td>${formatCurrency(transaction.commission)}</td>
            <td>${formatCurrency(transaction.netAmount)}</td>
        </tr>
    `).join('');
}

// Stocks
function loadStocks() {
    console.log('Loading stocks...');
    fetch('/api/stocks', { credentials: 'include' })
    .then(response => {
        console.log('Stocks response status:', response.status);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('Stocks data received:', data);
        updateStocksTable(data);
    })
    .catch(error => {
        console.error('Stocks error:', error);
        showToast('Failed to load stocks', 'error');
    });
}

function searchStocks() {
    const query = document.getElementById('stockSearch').value;
    
    if (!query.trim()) {
        loadStocks();
        return;
    }
    
    fetch(`/api/stocks/search?query=${encodeURIComponent(query)}`, { credentials: 'include' })
    .then(response => response.json())
    .then(data => {
        updateStocksTable(data);
    })
    .catch(error => {
        console.error('Search error:', error);
        showToast('Search failed', 'error');
    });
}

function updateStocksTable(stocks) {
    const tbody = document.querySelector('#stocksTable tbody');
    
    if (!stocks || stocks.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">No stocks found</td></tr>';
        return;
    }
    
    tbody.innerHTML = stocks.map(stock => `
        <tr>
            <td><strong>${stock.symbol}</strong></td>
            <td>${stock.name}</td>
            <td>${formatCurrency(stock.currentPrice)}</td>
            <td class="${stock.dayChange >= 0 ? 'positive' : 'negative'}">${formatCurrency(stock.dayChange)}</td>
            <td class="${stock.dayChangePercent >= 0 ? 'positive' : 'negative'}">${stock.dayChangePercent.toFixed(2)}%</td>
            <td>${stock.volume ? stock.volume.toLocaleString() : 'N/A'}</td>
            <td>${stock.marketCap ? formatCurrency(stock.marketCap) : 'N/A'}</td>
            <td>
                <button class="btn btn-outline btn-sm" onclick="tradeStock('${stock.symbol}')">
                    <i class="fas fa-exchange-alt"></i> Trade
                </button>
            </td>
        </tr>
    `).join('');
}

function tradeStock(symbol) {
    document.getElementById('stockSymbol').value = symbol;
    showSection('trading');
    loadStockInfo();
}

// Utility Functions
function formatCurrency(amount) {
    if (amount === null || amount === undefined) return '$0.00';
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toastContainer');
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.textContent = message;
    
    toastContainer.appendChild(toast);
    
    setTimeout(() => {
        toast.remove();
    }, 5000);
}

function showLoading(show) {
    const overlay = document.getElementById('loadingOverlay');
    overlay.style.display = show ? 'flex' : 'none';
}

function refreshData() {
    if (currentUser) {
        loadDashboard();
        showToast('Data refreshed', 'success');
    } else {
        showToast('Please login to refresh data', 'error');
    }
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Initialize sample data
function initializeSampleData() {
    fetch('/api/stocks/initialize', { method: 'POST' })
    .then(response => response.json())
    .then(data => {
        showToast(data.message, 'success');
        loadStocks();
    })
    .catch(error => {
        console.error('Initialize error:', error);
    });
}

// Close modal when clicking outside
window.onclick = function(event) {
    const modal = document.getElementById('authModal');
    if (event.target === modal) {
        closeAuthModal();
    }
}