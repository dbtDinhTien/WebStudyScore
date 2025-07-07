import React, { useEffect, useState } from "react";
import { Container, Form, Button, Alert, Spinner, ListGroup, Row, Col, Card } from "react-bootstrap";
import { authApis, endpoints } from "../configs/Apis";
import { useLocation, useNavigate } from "react-router-dom";

function useQuery() {
    return new URLSearchParams(useLocation().search);
}

const StudentChat = () => {
    const query = useQuery();
    const navigate = useNavigate();
    const receiverId = query.get("receiverId");

    const [senderId, setSenderId] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState("");
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [search, setSearch] = useState("");
    const [lecturers, setLecturers] = useState([]);
    const [conversationUsers, setConversationUsers] = useState([]);


    useEffect(() => {
        const fetchCurrentUser = async () => {
            try {
                const res = await authApis().get(endpoints['current-user']);
                setSenderId(res.data.id);
            } catch (err) {
                setError("Vui lòng đăng nhập để nhắn tin");
            }
        };
        fetchCurrentUser();
    }, []);

    useEffect(() => {
        const loadConversationUsers = async () => {
            if (!senderId) return;

            try {
                const res = await authApis().get(`${endpoints.chatConversations}?userId=${senderId}`);
                setConversationUsers(res.data);
            } catch (err) {
                console.error("Lỗi khi tải danh sách nhắn tin:", err);
            }
        };

        loadConversationUsers();
    }, [senderId]);


    useEffect(() => {
        const fetchMessages = async () => {
            try {
                setLoading(true);
                const res = await authApis().get(`${endpoints.chatMessages}?senderId=${senderId}&receiverId=${receiverId}`);

                // API đã xử lý loại bỏ trùng và sắp xếp -> dùng trực tiếp
                setMessages(res.data);
            } catch (err) {
                setError("Không thể tải tin nhắn");
            } finally {
                setLoading(false);
            }
        };
        if (senderId && receiverId) fetchMessages();
    }, [senderId, receiverId]);

    useEffect(() => {
        const loadLecturers = async () => {
            try {
                const res = await authApis().get(endpoints.lecturersSearch(search));
                setLecturers(res.data);
            } catch (err) {
                console.error(err);
            }
        };
        if (search.trim() !== "") {
            loadLecturers();
        }
    }, [search]);

    const sendMessage = async () => {
        if (!newMessage.trim() || !senderId) return;

        const msgData = { content: newMessage, type: "text" };

        try {
            await authApis().post(
                `${endpoints.chatSend}?senderId=${senderId}&receiverId=${receiverId}`,
                msgData
            );

            setMessages([...messages, {
                senderId,
                receiverId,
                content: newMessage,
                timestamp: Date.now()
            }]);
            const res = await authApis().get(`${endpoints.chatMessages}?senderId=${senderId}&receiverId=${receiverId}`);
            setMessages(res.data);
            setNewMessage("");
        } catch (err) {
            alert("Lỗi khi gửi tin nhắn");
        }
    };

    if (error === "Vui lòng đăng nhập để nhắn tin") {
        return <Alert variant="warning" className="mt-4 text-center">{error}</Alert>;
    }

    return (
        <Container className="mt-4">
            <h4 className="text-center mb-4 fw-bold display-6">💬 TIN NHẮN HỎI ĐÁP</h4>

            <Form.Control
                type="text"
                placeholder="🔍 Tìm giảng viên để nhắn tin..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                className="mb-3"
            />
            {conversationUsers.length > 0 && (
                <>
                    <h6>Những người đã nhắn tin</h6>
                    <ListGroup className="mb-3" style={{ maxHeight: "150px", overflowY: "auto" }}>
                        {conversationUsers.map(user => (
                            <ListGroup.Item
                                key={user.id}
                                action
                                onClick={() => navigate(`?receiverId=${user.id}`)}
                            >
                                {user.firstName} {user.lastName} ({user.email})
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                </>
            )}

            {lecturers.length > 0 && (
                <ListGroup className="mb-3">
                    {lecturers.map((lec) => (
                        <ListGroup.Item
                            key={lec.id}
                            action
                            onClick={() => {
                                navigate(`?receiverId=${lec.id}`);
                            }}
                        >
                            👨‍🏫 {lec.email}
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            )}

            {loading ? (
                <Spinner animation="border" />
            ) : (
                <>
                    <Card style={{ height: "400px", overflowY: "auto" }} className="mb-3 p-3 bg-light">
                        <ListGroup>
                            {messages.map((msg, idx) => (
                                <ListGroup.Item
                                    key={idx}
                                    style={{
                                        maxWidth: "70%",
                                        marginLeft: Number(msg.senderId) === Number(senderId) ? "auto" : "0",
                                        marginRight: Number(msg.senderId) === Number(senderId) ? "0" : "auto",
                                        backgroundColor: Number(msg.senderId) === Number(senderId) ? "#007bff" : "rgb(167, 169, 172)",
                                        color: Number(msg.senderId) === Number(senderId) ? "white" : "black",
                                        borderRadius: "15px",
                                        padding: "10px",
                                        marginBottom: "8px",
                                        wordBreak: "break-word"
                                    }}
                                >
                                    {msg.content}
                                    <div style={{ fontSize: "0.7em", color: "#ccc", marginTop: "5px" }}>
                                        {new Date(msg.timestamp).toLocaleString()}
                                    </div>
                                </ListGroup.Item>
                            ))}
                        </ListGroup>
                    </Card>

                    <Row>
                        <Col xs={10}>
                            <Form.Control
                                type="text"
                                placeholder="Nhập tin nhắn..."
                                value={newMessage}
                                onChange={(e) => setNewMessage(e.target.value)}
                            />
                        </Col>
                        <Col xs={2}>
                            <Button variant="success" onClick={sendMessage} disabled={!senderId || !receiverId}>Gửi</Button>
                        </Col>
                    </Row>
                </>
            )}
        </Container>
    );
};

export default StudentChat;
