import React, { useEffect, useState } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import UserService from '../services/UserService';

const PrivateRoute = ({ redirectPath }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [data, setData] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // 사용자의 토큰을 검증하여 인증 상태를 설정
        const token = UserService.getToken("accessToken");
        UserService.verifyToken(token)
            .then((res) => {
                setIsAuthenticated(true);
                setData(res.data);
                setLoading(false);
            })
            .catch(() => {
                setIsAuthenticated(false);
                setLoading(false);
            });
    }, []);  //비동기 처리해줘야 data가 저장됨. 근데 useEffect가 2번 실행되는 문제 발생- 기능상 문제는 없지만
    if (loading) {
        return <div>Loading...</div>; // 로딩 상태 표시
    }
    
    return isAuthenticated ? <Outlet context={{data}} /> : <Navigate to={redirectPath} />
};

export default PrivateRoute;
