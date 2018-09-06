package application;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class PopUpWindow {
	
	//private // = false;
	private static boolean willErase = false;
	
	public void popUp(String text) throws IOException{
		try {
			final JFrame frame = new JFrame();
			frame.setPreferredSize(new Dimension(450, 170));
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			JDialog dialog = new JDialog();
	        
	        JTextArea textArea = new JTextArea ();
	        textArea.setText(text);
	        textArea.setFont(new Font("arial", Font.PLAIN, 17));
	        textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setEditable(false);
			textArea.setBackground(UIManager.getColor("Label.background"));

	        JButton button = new JButton();
	        button.setSize(70, 55);
	        //button.setMargin(new Insets( 8, 8, 8, 8));
	        button.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
	        button.setText("OK");
	        
	        
	        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
	        button.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panel.add(textArea);
	        panel.add(button);
	        frame.add(panel);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setAlwaysOnTop(true);
	        frame.setResizable(false);

	        button.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	            }
	        });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//
	public static boolean deleteEventPopUp(String text) {
		try {
			final boolean willDelete = false;
			final JFrame frame = new JFrame();
			frame.setPreferredSize(new Dimension(450, 170));
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			JDialog dialog = new JDialog();
	        
	        JTextArea textArea = new JTextArea ();
	        textArea.setText(text);
	        textArea.setFont(new Font("arial", Font.PLAIN, 17));
	        textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setEditable(false);
			textArea.setBackground(UIManager.getColor("Label.background"));

	        JButton okButton = new JButton();
	        JButton cancelButton = new JButton();
	        okButton.setSize(70, 55);
	        cancelButton.setSize(70, 55);
	        okButton.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
	        cancelButton.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
	        okButton.setText("OK");
	        cancelButton.setText("Cancel");
	        
	        
	        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
	        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	        panel.add(textArea);
	        panel.add(okButton);
	        panel.add(cancelButton);
	        frame.add(panel);
	        frame.pack();
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setAlwaysOnTop(true);
	        frame.setResizable(false);
	        

	        okButton.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	setWillErase(true);
	            	System.out.println(willErase);
	            	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	            }
	        });
	        
	        cancelButton.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	setWillErase(false);
	            	
	            	frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	            }
	        });
	        frame.validate();
	        System.out.println(" ddddddd " + willErase);
	        return willErase;
	        
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public static boolean getWillErase() {
		return willErase;
	}

	private static void setWillErase(boolean willErase) {
		PopUpWindow.willErase = willErase;
	}

}
