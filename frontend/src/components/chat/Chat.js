import React, { useState } from 'react';
import styles from './css/Chat.module.css';
import chatIcon from './images/chat.png';

const Chat = ({ isOpen, onClose }) => {
  const [messages, setMessages] = useState([]);
  const [inputValue, setInputValue] = useState(""); // 입력 필드 상태

  // 메시지 전송 함수
  const handleSendMessage = () => {
    if (inputValue.trim() === "") return; // 빈 메시지 전송 방지

    const newMessage = { text: inputValue, sender: "user" };
    setMessages([...messages, newMessage]); // 메시지 추가

    // Kafka를 통해 관리자에게 메시지 전송하는 로직 추가
    // sendToKafka(inputValue); // Kafka 전송 함수 호출

    setInputValue(""); // 전송 후 입력 필드 비우기
  };

  return (
    isOpen ? (
      <div className={styles.chatModal}>
        <div className={styles.chatHeader}>
          <span>관리자와의 채팅</span>
          <button onClick={onClose} className={styles.closeButton}>X</button>
        </div>
        <div className={styles.chatContent}>
          {messages.map((msg, index) => (
            <div
              key={index}
              className={msg.sender === "admin" ? styles.adminMessage : styles.userMessage}
            >
              {msg.text}
            </div>
          ))}
        </div>
        <div className={styles.chatInput}>
          <input
            type="text"
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)} // 입력 값 업데이트
            placeholder="메시지를 입력하세요"
          />
          <button onClick={handleSendMessage}>전송</button>
        </div>
      </div>
    ) : (
      <div onClick={onClose} className={styles.chatIcon}>
        <img src={chatIcon} alt="Chat Icon" />
      </div>
    )
  );
};

export default Chat;
