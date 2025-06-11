import React, { useState } from 'react';
import { TfiMenu } from "react-icons/tfi";
import ContentCollector from '../components/admin/ContentCollector';
import logo from '../images/logo.PNG'
import './AdminPage.css'
import { MenuOutlined } from '@ant-design/icons';

function AdminLayout() {
  const [selectedMenu, setSelectedMenu] = useState('content');
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const menuItems = [
    { id: 'content', label: '콘텐츠 관리' },
    { id: 'users', label: '사용자 관리' },
    { id: 'statistics', label: '통계' },
    { id: 'settings', label: '설정' }
  ];

  const renderContent = () => {
    switch (selectedMenu) {
      case 'content':
        return <ContentCollector />;
      case 'users':
        return <div>사용자 관리 페이지</div>;
      case 'statistics':
        return <div>통계 페이지</div>;
      case 'settings':
        return <div>설정 페이지</div>;
      default:
        return <ContentCollector />;
    }
  };

  const handleMenuClick = (menuId) => {
    setSelectedMenu(menuId);
    setIsSidebarOpen(false);
  };

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <div className="layout">
      <header className="header">
        <div className="header-left">
          <button className="menu-button" onClick={toggleSidebar}>
            <MenuOutlined />
          </button>
          <img src={logo} alt="Logo" className="logo" />
        </div>
        <div className="profile">
          <span className="profile-name">관리자</span>
        </div>
      </header>
      <aside className={`sider ${isSidebarOpen ? 'open' : ''}`}>
        <div className="menu">
          {menuItems.map((item) => (
            <div
              key={item.id}
              className={`menu-item ${selectedMenu === item.id ? 'active' : ''}`}
              onClick={() => handleMenuClick(item.id)}
            >
              {item.label}
            </div>
          ))}
        </div>
      </aside>
      <div className={`content ${isSidebarOpen ? 'sidebar-open' : ''}`}>
        {renderContent()}
      </div>
    </div>
  );
}

export default AdminLayout;
