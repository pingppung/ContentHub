import CrawlButton from '../components/CrawlButton';
function AdminLayout({name}) {
    return (
        <div className="admin-layout">
            <div>{name}님은 admin 인증된 사람입니다</div>
        <CrawlButton />
      </div>
    );
}
export default AdminLayout;
