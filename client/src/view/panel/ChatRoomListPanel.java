package view.panel;

import app.Application;
import domain.ChatRoom;
import dto.request.EnterChatRequest;
import view.frame.ChatFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChatRoomListPanel extends JPanel {

    JPanel labelPanel;

    public ChatRoomListPanel(JFrame frame) {
        setLayout(null);

        // 채팅 메시지 영역 (스크롤)
        labelPanel = new JPanel();
        labelPanel.setSize(400, 250);
        labelPanel.setBackground(Color.MAGENTA);
        labelPanel.setLayout(new GridLayout(30, 1));
        for (int i=0; i<30; i++) {
            labelPanel.add(new JLabel("Hell" + i));
        }

        JScrollPane scrPane = new JScrollPane(labelPanel);
        scrPane.setBounds(0, 0, 400, 250);
        scrPane.setBackground(Color.MAGENTA);
        add(scrPane);

        frame.add(this);

        setBackground(Color.BLUE);
        setBounds(410, 210, 400, 250);
    }

    public void paintChatRoomList() {
        labelPanel.removeAll();

        for (ChatRoom chatRoom : Application.chatRooms) {
            JLabel label = new JLabel(chatRoom.getName());
            label.addMouseListener(new ChatRoomMouseAdapter(chatRoom.getName()));
            labelPanel.add(label);
        }

        revalidate();
    }

    public void addChatRoomLabel(String chatRoomName) {
        JLabel label = new JLabel(chatRoomName);
        label.addMouseListener(new ChatRoomMouseAdapter(chatRoomName));
        labelPanel.add(label);

        revalidate();
    }

    // 채팅방 레이블을 누르면, 해당 채팅방 접속
    class ChatRoomMouseAdapter extends MouseAdapter {

        String chatRoomName;

        public ChatRoomMouseAdapter(String chatRoomName) {
            this.chatRoomName = chatRoomName;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (Application.chatPanelMap.containsKey(chatRoomName)) {
                System.out.println("[" + chatRoomName + "] 채팅방 이미 열려있음");
                return;
            }

            ChatFrame chatFrame = new ChatFrame(chatRoomName);
            Application.chatPanelMap.put(chatRoomName, chatFrame.getChatPanel()); // 채팅방 화면 관리
            Application.chatRoomUserListPanelMap.put(chatRoomName, chatFrame.getChatRoomUserListPanel()); // 채팅방 사용자 리스트 관리

            Application.sender.sendMessage(new EnterChatRequest(chatRoomName, Application.me.getId()));
        }
    }
}
