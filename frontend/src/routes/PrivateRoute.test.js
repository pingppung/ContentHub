import React from "react";
import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import PrivateRoute from "./PrivateRoute";
import AuthService from "../services/AuthService";

jest.mock("../services/AuthService", () => ({
  getToken: jest.fn(), getCurrentUser: jest.fn(), removeToken: jest.fn(),
}));

function renderRoute(path) {
  return render(<MemoryRouter initialEntries={[path]}>
    <Routes>
      <Route element={<PrivateRoute redirectPath="/auth/login" />}>
        <Route path="/user" element={<div>회원 페이지</div>} />
        <Route path="/admin" element={<div>관리자 페이지</div>} />
      </Route>
      <Route path="/auth/login" element={<div>로그인 화면</div>} />
    </Routes>
  </MemoryRouter>);
}

beforeEach(() => {
  jest.clearAllMocks();
  AuthService.getToken.mockReturnValue("test-token");
});

test("본인 정보 조회 성공 후 회원 페이지로 이동한다", async () => {
  AuthService.getCurrentUser.mockResolvedValue({ userId: "member", role: "ROLE_USER" });
  renderRoute("/user");
  expect(await screen.findByText("회원 페이지")).toBeTruthy();
  expect(AuthService.getCurrentUser).toHaveBeenCalledWith("test-token");
});

test("일반 회원은 관리자 페이지에 들어갈 수 없다", async () => {
  AuthService.getCurrentUser.mockResolvedValue({ userId: "member", role: "ROLE_USER" });
  renderRoute("/admin");
  expect(await screen.findByText("로그인 화면")).toBeTruthy();
});

test("관리자는 관리자 페이지에 들어갈 수 있다", async () => {
  AuthService.getCurrentUser.mockResolvedValue({ userId: "admin", role: "ROLE_ADMIN" });
  renderRoute("/admin");
  expect(await screen.findByText("관리자 페이지")).toBeTruthy();
});

test("인증 실패 시 토큰을 지우고 로그인 화면으로 이동한다", async () => {
  AuthService.getCurrentUser.mockRejectedValue({ response: { status: 401 } });
  renderRoute("/user");
  expect(await screen.findByText("로그인 화면")).toBeTruthy();
  expect(AuthService.removeToken).toHaveBeenCalledTimes(1);
});
