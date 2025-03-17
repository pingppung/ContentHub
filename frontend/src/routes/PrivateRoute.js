import React, { useEffect, useState } from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import AuthService from '../services/AuthService';

const PrivateRoute = ({ redirectPath }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const [userInfo, setUserInfo] = useState("");
    const [loading, setLoading] = useState(true);
    const location = useLocation();

    useEffect(() => {
        const token = AuthService.getToken("accessToken");
        if (!token) {
            setIsAuthenticated(false);
            setLoading(false);
            return;
        }
        AuthService.verifyToken(token)
            .then((res) => {
                console.log(res);
                setUserInfo(res.data.userId);
                setIsAuthenticated(true);
                setLoading(false);
                // 사용자 정보를 기반으로 어드민 여부를 확인하고 처리
                if (res.data.authorities.some(authority => authority.authority === "ROLE_ADMIN")) {
                    console.log("관리자 권한이 있음");
                    handleAdminActions();
                }
            })
            .catch((e) => {
                console.log("사용자 오류:", e);
                setIsAuthenticated(false);
                setLoading(false);
            });
    }, []);  //비동기 처리해줘야 data가 저장됨. 근데 useEffect가 2번 실행되는 문제 발생- 기능상 문제는 없지만
    const handleAdminActions = () => {
        setIsAdmin(true);
        const token = AuthService.getToken("accessToken");
        if (!token) {
            setIsAuthenticated(false);
            setLoading(false);
            return;
        }
    
        AuthService.verifyAuth(token)
            .then((res) => {
                console.log("어드민 확인:", res.data);
                // 여기서 어드민 관련 작업을 수행
            })
            .catch((e) => {
                console.log("어드민 오류:", e);
            });
    };
    if (loading) {
        return <div>Loading...</div>; // 로딩 상태 표시
    }
    if (location.pathname === '/admin') { //호출한 url이 /admin이면 

        return isAdmin ? < Outlet /> : <Navigate to={redirectPath} />;
    }
    return isAuthenticated ? < Outlet/> : <Navigate to={redirectPath} />
};

export default PrivateRoute;
