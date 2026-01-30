// Lightweight page layout component with a content slot
// Usage in a page:
// <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
// <script src="../components/app-layout.js"></script>
// <app-layout active="areas" breadcrumb="首页 / 系统管理 / 区域管理" subtext="请选择左侧或在页面内进行操作">
//   <div slot="content">...your page body (cards, toolbars, tables, modals)...</div>
// </app-layout>

class AppLayout extends HTMLElement {
  connectedCallback() {
    const active = this.getAttribute('active') || ''; // dashboard | users | departments | roles | areas | devices
    const breadcrumb = this.getAttribute('breadcrumb') || '首页';

    // Determine base paths for links depending on current page location
    const inPages = /\/src\/pages\//.test(location.pathname);
    const paths = inPages ? {
      dashboard: '../index.html',
      users: 'system-users.html',
      departments: 'system-departments.html',
      roles: 'system-roles.html',
      areas: 'system-areas.html',
      devices: 'system-devices.html',
      detection: 'device-monitor.html',
      dangerEvents: 'danger-events.html',
      detectionRules: 'alert-rule.html',
      alertMessages: 'alert-messages.html',
      emergencyCenter: 'emergency-center.html',
      emergencyDispatch: 'emergency-dispatch.html',
      emergencyReports: 'emergency-reports.html'
    } : {
      dashboard: 'index.html',
      users: 'pages/system-users.html',
      departments: 'pages/system-departments.html',
      roles: 'pages/system-roles.html',
      areas: 'pages/system-areas.html',
      devices: 'pages/system-devices.html',
      detection: 'pages/device-monitor.html',
      dangerEvents: 'pages/danger-events.html',
      detectionRules: 'pages/alert-rule.html',
      alertMessages: 'pages/alert-messages.html',
      emergencyCenter: 'pages/emergency-center.html',
      emergencyDispatch: 'pages/emergency-dispatch.html',
      emergencyReports: 'pages/emergency-reports.html'
    };

    // Use Shadow DOM so the slot works and page content can project into the layout
    const root = this.shadowRoot || this.attachShadow({ mode: 'open' });
    const logoPath = inPages ? '../assets/logo-hrecs-radar.svg' : 'assets/logo-hrecs-radar.svg';
    root.innerHTML = `
      <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
      <style>
        :host { --blue-6:#2F54EB; --blue-5:#3B6AE8; --bg-body:#f6f8fc; --bg-card:#fff; --border:#e5e7eb; --text:#374151; --muted:#6b7280; --radius:12px; --shadow:0 10px 30px rgba(37,99,235,.12); --logo-size:48px; }
        .app { min-height:100vh; display:grid; grid-template-columns:240px 1fr; background:var(--bg-body, #f6f8fc); font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,'Helvetica Neue',Arial,'Noto Sans','PingFang SC','Microsoft YaHei',sans-serif; }
        .sider { background:var(--bg-card, #fff); border-right:1px solid var(--border, #e5e7eb); padding:18px 16px; }
        .sider-header { display:flex; align-items:center; gap:10px; padding:8px 10px; }
        .logo-img { width: var(--logo-size, 48px); height: var(--logo-size, 48px); border-radius:6px; object-fit: contain; }
        .title { font-weight:600; color:#1f2937; }
        .menu-list { list-style:none; margin:16px 0 0; padding:0; }
        .menu-item { display:flex; align-items:center; gap:10px; padding:10px 12px; border-radius:10px; cursor:pointer; color:#374151; text-decoration:none; }
        .menu-item:hover { background:#f0f5ff; }
        .menu-item.active { background:#e6f0ff; color:var(--blue-6, #2F54EB); }
        .menu-group-title { margin-top:8px; padding:8px 12px; color:#6b7280; font-weight:600; font-size:13px; display:flex; align-items:center; gap:8px; }
        .submenu-list { list-style:none; margin:0; padding:0 0 0 8px; }

        .main { height:100vh; display:grid; grid-template-rows:64px 1fr; }
        .header { background:var(--bg-card, #fff); border-bottom:1px solid var(--border, #e5e7eb); display:flex; align-items:center; justify-content:space-between; padding:0 16px; }
        .breadcrumb { display:flex; align-items:center; gap:8px; font-size:14px; color:var(--text, #374151); }
        .header-left { display:flex; align-items:center; gap:12px; }
        .header-right { display:flex; align-items:center; gap:16px; }
        .bell-wrap { position:relative; }
        .bell { width:28px; height:28px; border-radius:50%; background:#f0f5ff; display:flex; align-items:center; justify-content:center; color: var(--blue-6); cursor:pointer; position:relative; }
        .bell .badge { position:absolute; right:-6px; top:-6px; background:#ff4d4f; color:#fff; border-radius:10px; padding:0 6px; font-size:11px; line-height:16px; height:16px; min-width:16px; text-align:center; }
        .alert-pop { position:absolute; right:0; top:38px; background:#fff; border:1px solid var(--border); border-radius:12px; box-shadow:var(--shadow); display:none; width:360px; max-height:360px; overflow:auto; z-index:30; }
        .bell-wrap:hover .alert-pop { display:block; }
        .alert-pop .head { padding:10px 12px; border-bottom:1px solid var(--border); font-weight:600; color:#1f2937; display:flex; align-items:center; justify-content:space-between; }
        .alert-pop .list { padding:8px; display:flex; flex-direction:column; gap:8px; }
        .alert-pop .item { display:flex; align-items:flex-start; gap:8px; padding:8px; border:1px solid #f0f0f0; border-radius:10px; cursor:pointer; }
        .alert-pop .item:hover { background:#fafafa; }
        .alert-pop .meta { color:#64748b; font-size:12px; }
        .alert-pop .actions { margin-left:auto; display:flex; gap:8px; }
        /* Button styles inside popover */
        .btn { appearance:none; -webkit-appearance:none; border:none; outline:none; background:transparent; color:#374151; padding:6px 12px; border-radius:8px; font-size:13px; line-height:20px; cursor:pointer; transition:all .2s ease; box-shadow:none; }
        .btn:hover { background:#f5f5f5; }
        .btn-primary { background:var(--blue-6); color:#fff; box-shadow:0 6px 16px rgba(47,84,235,.25); }
        .btn-primary:hover { background:#1d39c4; box-shadow:0 8px 18px rgba(47,84,235,.35); }
        .btn-primary:active { transform:translateY(1px); box-shadow:0 4px 12px rgba(47,84,235,.25); }
        .avatar-wrap { position:relative; }
        .avatar { width:32px; height:32px; border-radius:50%; background:#c1d4ff; display:flex; align-items:center; justify-content:center; color:#fff; font-weight:600; cursor:pointer; }
        .user-menu { position:absolute; right:0; top:38px; background:#fff; border:1px solid var(--border); border-radius: 12px; box-shadow: var(--shadow); list-style:none; padding:6px 0; margin:0; display:none; min-width:140px; z-index:20; }
        .avatar-wrap:hover .user-menu { display:block; }
        .user-menu li { padding:8px 12px; font-size:14px; color:#374151; cursor:pointer; }
        .user-menu li:hover { background:#f5f5f5; }
        .content { padding:16px; height:100%; box-sizing:border-box; display:flex; flex-direction:column;flex:1; min-height:0 }
        ::slotted(*) { height:100%; display:flex; flex-direction:column; }
      </style>
      <div class="app">
        <aside class="sider">
          <div class="sider-header"><img class="logo-img" src="${logoPath}" alt="系统Logo"><div class="title">高速公路危害识别系统</div></div>
          <ul class="menu-list">
            <li><a class="menu-item ${active==='dashboard'?'active':''}" href="${paths.dashboard}"><i class="fa-solid fa-house"></i><span>仪表盘</span></a></li>
            <li class="menu-group">
              <div class="menu-group-title"><i class="fa-solid fa-screwdriver-wrench"></i><span>系统管理</span></div>
              <ul class="submenu-list">
                <li><a class="menu-item ${active==='departments'?'active':''}" href="${paths.departments}"><i class="fa-solid fa-sitemap"></i><span>部门管理</span></a></li>
                <li><a class="menu-item ${active==='roles'?'active':''}" href="${paths.roles}"><i class="fa-solid fa-user-shield"></i><span>角色管理</span></a></li>
                <li><a class="menu-item ${active==='users'?'active':''}" href="${paths.users}"><i class="fa-solid fa-users"></i><span>用户管理</span></a></li>
                <li><a class="menu-item ${active==='areas'?'active':''}" href="${paths.areas}"><i class="fa-solid fa-road"></i><span>区域管理</span></a></li>
                <li><a class="menu-item ${active==='devices'?'active':''}" href="${paths.devices}"><i class="fa-solid fa-microchip"></i><span>设备管理</span></a></li>
              </ul>
            </li>
            <li class="menu-group">
              <div class="menu-group-title"><i class="fa-solid fa-radiation"></i><span>危险检测</span></div>
              <ul class="submenu-list">
                <li><a class="menu-item ${active==='detection'?'active':''}" href="${paths.detection}"><i class="fa-solid fa-video"></i><span>设备监控</span></a></li>
                <li><a class="menu-item ${active==='danger-events'?'active':''}" href="${paths.dangerEvents}"><i class="fa-solid fa-table"></i><span>事件流</span></a></li>
              </ul>
            </li>
            <li class="menu-group">
              <div class="menu-group-title"><i class="fa-solid fa-triangle-exclamation"></i><span>危险告警</span></div>
              <ul class="submenu-list">
                <li><a class="menu-item ${active==='detection-rules'?'active':''}" href="${paths.detectionRules}"><i class="fa-solid fa-sliders"></i><span>告警规则配置</span></a></li>
                <li><a class="menu-item ${active==='alert-messages'?'active':''}" href="${paths.alertMessages}"><i class="fa-solid fa-bell"></i><span>告警消息</span></a></li>
              </ul>
            </li>
            <li class="menu-group">
              <div class="menu-group-title"><i class="fa-solid fa-helmet-safety"></i><span>应急指挥</span></div>
              <ul class="submenu-list">
                <li><a class="menu-item ${active==='emergency-center'?'active':''}" href="${paths.emergencyCenter}"><i class="fa-solid fa-chalkboard"></i><span>事件中心</span></a></li>
                <li><a class="menu-item ${active==='emergency-dispatch'?'active':''}" href="${paths.emergencyDispatch}"><i class="fa-solid fa-route"></i><span>资源绑定</span></a></li>
                <li><a class="menu-item ${active==='emergency-reports'?'active':''}" href="${paths.emergencyReports}"><i class="fa-solid fa-file-lines"></i><span>报告复盘</span></a></li>
              </ul>
            </li>
          </ul>
        </aside>
        <main class="main">
          <div class="header">
            <div class="header-left">
              <div class="breadcrumb">${breadcrumb}</div>
            </div>
            <div class="header-right">
              <div class="bell-wrap">
                <div class="bell" title="告警"><i class="fa-solid fa-bell"></i><span class="badge" style="display:none;" id="alertBadge">0</span></div>
                <div class="alert-pop">
                  <div class="head">待确认告警<span class="muted" id="alertCount" style="font-weight:400;color:#6b7280"></span></div>
                  <div class="list" id="alertList"></div>
                </div>
              </div>
              <div class="avatar-wrap">
                <div class="avatar" title="用户">F</div>
                <ul class="user-menu">
                  <li id="changePwd">修改密码</li>
                  <li id="logout">退出登录</li>
                </ul>
              </div>
            </div>
          </div>
          <div class="content">
            <slot name="content"></slot>
          </div>
        </main>
      </div>
    `;

    // Alert popover: load messages, filter need-confirm (status==='open'), render list
    const alertDetailPath = inPages ? 'alert-detail.html' : 'pages/alert-detail.html';
    const storeKey = 'alert_messages_v1';
    function loadAlerts(){ try{ return JSON.parse(localStorage.getItem(storeKey)||'[]'); }catch(e){ return []; } }
    function saveAlerts(list){ localStorage.setItem(storeKey, JSON.stringify(list||[])); }
    function renderAlertPopover(){ const listBox = root.querySelector('#alertList'); const badge = root.querySelector('#alertBadge'); const cnt = root.querySelector('#alertCount'); if(!listBox) return; const all=loadAlerts(); const need = all.filter(x=>x.status==='open');
      if (badge){ badge.textContent = String(need.length||0); badge.style.display = need.length? 'inline-block':'none'; }
      if (cnt) cnt.textContent = need.length? `（${need.length}）` : '（0）';
      listBox.innerHTML=''; if(!need.length){ const empty=document.createElement('div'); empty.className='muted'; empty.style.padding='8px 10px'; empty.textContent='暂无待确认告警'; listBox.appendChild(empty); return; }
      need.sort((a,b)=> new Date(b.occurredAt)-new Date(a.occurredAt)).forEach(m=>{
        const item=document.createElement('div'); item.className='item'; item.innerHTML=`
          <div style="flex:1;">
            <div style="font-weight:600;color:#1f2937;display:flex;align-items:center;gap:8px"><i class="fa-solid fa-bell"></i><span>${m.name}</span><span class="level-badge" style="background:#fff1f0;color:#f5222d;border:1px solid #ffa39e;padding:2px 6px;border-radius:10px;font-size:12px;">${m.level}</span></div>
            <div class="meta">${m.category} · ${m.device} · ${new Date(m.occurredAt).toLocaleString()}</div>
          </div>
          <div class="actions">
            <button class="btn btn-primary">确认</button>
          </div>
        `;
        item.addEventListener('click', (e)=>{ if (e.target && e.target.tagName==='BUTTON') return; window.location.href = `${alertDetailPath}?id=${encodeURIComponent(m.id)}`; });
        const btn = item.querySelector('button'); if (btn) btn.addEventListener('click', (e)=>{ e.stopPropagation(); const all2=loadAlerts(); const idx=all2.findIndex(x=>x.id===m.id); if(idx<0) return; all2[idx].status='processing'; all2[idx].ackBy='dispatcher-01'; all2[idx].ackAt=new Date().toISOString(); saveAlerts(all2); renderAlertPopover(); });
        listBox.appendChild(item);
      });
    }
    renderAlertPopover();
    // Basic interactions for header actions
    const bell = root.querySelector('.bell');
    if (bell) bell.addEventListener('click', () => { /* 点击铃铛不跳转，保留悬浮弹层 */ });
    const changePwd = root.querySelector('#changePwd');
    if (changePwd) changePwd.addEventListener('click', () => alert('修改密码功能待实现'));
    const logout = root.querySelector('#logout');
    if (logout) logout.addEventListener('click', () => alert('已退出登录（原型）'));
  }
}

customElements.define('app-layout', AppLayout);

// Global Message component (Ant Design Vue-like)
(function initGlobalMessage(){
  if (window.Message) return;
  // Inject global styles once
  if (!document.getElementById('global-message-styles')) {
    const style = document.createElement('style');
    style.id = 'global-message-styles';
    style.textContent = `
      .ui-message-container { position: fixed; left: 0; right: 0; top: 24px; z-index: 1000; display: flex; flex-direction: column; align-items: center; gap: 8px; pointer-events: none; }
      .ui-message { min-width: 240px; max-width: 640px; box-sizing: border-box; padding: 10px 14px; border-radius: 8px; border: 1px solid #f0f0f0; box-shadow: 0 6px 16px rgba(0,0,0,.08); display: flex; align-items: center; gap: 10px; font-size: 14px; line-height: 22px; background: #fff; color: rgba(0,0,0,.88); opacity: 0; transform: translateY(-6px); transition: all .2s ease; pointer-events: auto; }
      .ui-message.show { opacity: 1; transform: translateY(0); }
      .ui-message.leave { opacity: 0; transform: translateY(-6px); }
      .ui-message .icon { width: 18px; height: 18px; display: inline-flex; align-items: center; justify-content: center; }
      .ui-message.info .icon { color: #1677ff; }
      .ui-message.success .icon { color: #52c41a; }
      .ui-message.warning .icon { color: #faad14; }
      .ui-message.error .icon { color: #ff4d4f; }
      @media (max-width: 480px) { .ui-message { width: calc(100% - 32px); } }
    `;
    document.head.appendChild(style);
  }
  // Ensure container exists
  function ensureContainer(){
    let box = document.querySelector('.ui-message-container');
    if (!box) { box = document.createElement('div'); box.className = 'ui-message-container'; document.body.appendChild(box); }
    return box;
  }
  function iconByType(type){
    switch(type){
      case 'success': return 'fa-solid fa-check-circle';
      case 'warning': return 'fa-solid fa-exclamation-circle';
      case 'error': return 'fa-solid fa-xmark-circle';
      default: return 'fa-solid fa-info-circle';
    }
  }
  function open(opts){
    const { type='info', content='', duration=2, closable=false } = (opts||{});
    const box = ensureContainer();
    const el = document.createElement('div');
    el.className = `ui-message ${type}`;
    el.innerHTML = `<span class="icon"><i class="${iconByType(type)}"></i></span><span class="text">${content}</span>`;
    box.appendChild(el);
    requestAnimationFrame(()=> el.classList.add('show'));
    let timer = null;
    const remove = () => {
      if (!el.isConnected) return;
      el.classList.remove('show');
      el.classList.add('leave');
      setTimeout(()=>{ try{ el.remove(); }catch(e){} }, 180);
    };
    if (!closable) timer = setTimeout(remove, Math.max(0, duration*1000));
    return { close: remove };
  }
  const api = {
    open,
    info: (content, duration=2) => open({ type:'info', content, duration }),
    success: (content, duration=2) => open({ type:'success', content, duration }),
    warning: (content, duration=2) => open({ type:'warning', content, duration }),
    error: (content, duration=2) => open({ type:'error', content, duration })
  };
  window.Message = api;
  window.showMessage = (type, content, duration=2) => api[type] ? api[type](content, duration) : api.info(content, duration);
})();