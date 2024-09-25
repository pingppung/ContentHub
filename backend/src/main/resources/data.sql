insert into site (name, url)
values ('카카오페이지', 'https://page.kakao.com')
on conflict (name) do nothing;

insert into site (name, url)
values ('네이버시리즈', 'https://series.naver.com')
on conflict (name) do nothing;
