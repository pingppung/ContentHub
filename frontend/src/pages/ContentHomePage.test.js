import React from "react";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import axios from "axios";
import ContentHome from "./ContentHomePage";

jest.mock("axios", () => ({ get: jest.fn() }));
jest.mock("../components/common/Header", () => () => null);
jest.mock("../components/content/PopularContent", () => () => null);
jest.mock("../components/content/Content", () => ({ title }) => <div>{title}</div>);

function renderHome(path = "/contents/novel") {
  return render(
    <MemoryRouter initialEntries={[path]}>
      <Routes>
        <Route path="/" element={<ContentHome />} />
        <Route path="/contents/:category" element={<ContentHome />} />
      </Routes>
    </MemoryRouter>
  );
}

beforeEach(() => {
  jest.clearAllMocks();
  axios.get.mockResolvedValue({ data: [{ title: "기본 작품", type: "NOVEL" }] });
});

test("홈에서 전체 목록을 한 번 요청하고 인기 영역과 공유한다", async () => {
  renderHome("/");
  await screen.findAllByText("기본 작품");
  expect(axios.get).toHaveBeenCalledTimes(1);
  expect(axios.get).toHaveBeenCalledWith("/contents", { params: {} });
});

test("필터 요청에 전체 장르를 보내지 않고 필터 해제 시 기본 목록을 재사용한다", async () => {
  renderHome();
  await screen.findAllByText("기본 작품");
  axios.get.mockResolvedValueOnce({ data: [{ title: "로맨스 작품", type: "NOVEL" }] });
  fireEvent.click(screen.getByRole("button", { name: "로맨스" }));
  await screen.findByText("로맨스 작품");
  expect(axios.get).toHaveBeenLastCalledWith("/contents", {
    params: { type: "NOVEL", genre: "로맨스" }
  });
  fireEvent.click(screen.getByRole("button", { name: "전체" }));
  await waitFor(() => expect(screen.queryByRole("status")).toBeNull());
  expect(screen.queryByText("로맨스 작품")).toBeNull();
  expect(axios.get).toHaveBeenCalledTimes(2);

  fireEvent.click(screen.getByRole("link", { name: "웹툰" }));
  await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(3));
  expect(axios.get).toHaveBeenLastCalledWith("/contents", { params: { type: "WEBTOON" } });
});

test("비정상 응답도 빈 목록으로 처리해 로딩을 종료한다", async () => {
  axios.get.mockResolvedValueOnce({ data: null });
  renderHome();
  await waitFor(() => expect(screen.queryByRole("status")).toBeNull());
  expect(screen.getByText("전체 콘텐츠")).toBeTruthy();
});

test("요청 실패를 알리고 재시도하면 실패한 기본 목록을 다시 요청한다", async () => {
  const errorLog = jest.spyOn(console, "error").mockImplementation(() => {});
  try {
    axios.get.mockRejectedValueOnce(new Error("Server error"));
    renderHome("/");
    expect(await screen.findByRole("alert")).toBeTruthy();
    fireEvent.click(screen.getByRole("button", { name: "다시 시도" }));
    await screen.findAllByText("기본 작품");
    expect(screen.queryByRole("alert")).toBeNull();
    expect(axios.get).toHaveBeenCalledTimes(2);
  } finally {
    errorLog.mockRestore();
  }
});
