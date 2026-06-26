@echo off
title He Thong Chat Socket - Kiem Thu Tu Dong

:: 1. Định nghĩa đường dẫn đến thư mục chứa code compiled (chứa thư mục server và client)
set PROJECT_DIR="C:\Users\HP\Desktop\build_chat_TVU\build_Chat\src_chat_nhom"

echo Dang khoi dong Server tai cong 5000...
:: Khởi động Server trong một cửa sổ cmd mới
start "SERVER" cmd /k "cd /d %PROJECT_DIR% && java server.Server"

:: Chờ 2 giây để đảm bảo Server đã bật lên trước khi các Client kết nối
timeout /t 2 /nobreak > nul

echo Dang khoi dong cac Client...
:: Khởi động Client 1 (Trung)
start "No.1" cmd /k "cd /d %PROJECT_DIR% && java client.Client"

:: Khởi động Client 2 (Linh)
start "No.2" cmd /k "cd /d %PROJECT_DIR% && java client.Client"

:: Khởi động Client 3 (Son)
start "No.3" cmd /k "cd /d %PROJECT_DIR% && java client.Client"

echo Tat ca cac cua so da duoc mo!
pause
exit