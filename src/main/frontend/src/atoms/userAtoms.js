import { atom } from 'recoil';

// 유저 정보 객체를 포함한 Recoil atom 정의
export const userDataState = atom({
  key: 'userDataState',
  default: {
    username: '',
    email: '',
    role: 'user',  // 기본적으로 사용자 역할
    // 기타 필요한 정보 추가 가능
  },
});

// 사용자 로그인 상태를 관리하는 Recoil atom
export const userLoggedInState = atom({
  key: 'userLoggedInState',
  default: false,
});