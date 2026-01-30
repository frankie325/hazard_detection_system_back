// 主要JavaScript功能

// 侧边栏切换
function toggleSidebar() {
  document.querySelector('.sidebar').classList.toggle('open');
}

// 简单路由系统
const routes = {
  '/': '/src/pages/dashboard.html',
  '/system': '/src/pages/system-management.html',
  '/alert': '/src/pages/alert-management.html',
  '/detection': '/src/pages/device-monitor.html',
  '/dashboard': '/src/pages/dashboard.html',
  '/emergency': '/src/pages/emergency-management.html'
};

// 加载页面内容
async function loadPage(url) {
  const contentDiv = document.getElementById('content');
  try {
    const response = await fetch(url);
    const html = await response.text();
    contentDiv.innerHTML = html;
    // 更新活动菜单项
    updateActiveMenuItem(url);
  } catch (error) {
    contentDiv.innerHTML = '<div class="p-4 text-red-500">页面加载失败</div>';
  }
}

// 更新活动菜单项
function updateActiveMenuItem(url) {
  const menuItems = document.querySelectorAll('.nav-item');
  menuItems.forEach(item => {
    item.classList.remove('bg-blue-700');
    if (item.getAttribute('data-route') === getRouteFromUrl(url)) {
      item.classList.add('bg-blue-700');
    }
  });
}

// 从URL获取路由
function getRouteFromUrl(url) {
  for (const route in routes) {
    if (routes[route] === url) {
      return route;
    }
  }
  return '/';
}

// 初始化路由
function initRouter() {
  document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('click', (e) => {
      e.preventDefault();
      const route = item.getAttribute('data-route');
      navigateTo(route);
    });
  });

  // 默认加载仪表盘页面
  navigateTo('/');
}

// 导航到指定路由
function navigateTo(route) {
  const url = routes[route] || routes['/'];
  loadPage(url);
  // 更新浏览器历史记录
  window.history.pushState({}, '', route);
}

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', () => {
  initRouter();
  
  // 侧边栏切换按钮
  document.getElementById('sidebarToggle').addEventListener('click', toggleSidebar);
});