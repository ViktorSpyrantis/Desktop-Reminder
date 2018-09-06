package application;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * This class
 */


public class Alarm {

	private static DateFormat  oldDateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm aa", Locale.ENGLISH);
	private static DateFormat  newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
	private static boolean bool = false;
	private static PopUpWindow popUpObject = new PopUpWindow();

	final static String OLD_FORMAT = "yyyy-MM-dd h:mm a";
	final static String NEW_FORMAT = "yyyy-MM-dd HH:mm";


	static{

		Thread t = new Thread(new Runnable() {
            public void run() {
            	try {
            		while(!bool){
            			timeCheck();
            		}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }

		});
        t.start();


	}

	//checks if the  current time is between the 
	private static void timeCheck() throws SQLException, ParseException, InterruptedException, IOException{
		String dateAndTimeStr = DataBase.getNextDateTime();
		String eventSubject = DataBase.getEventSubject();

		Date currentDate = new Date();
		//String cdt = oldDateFormat.format(currentDate);
		//Date currentDateTime = oldDateFormat.parse(cdt);
		//String cdt2 = oldDateFormat.format(currentDateTime);
		Date onGoingDate = oldDateFormat.parse(dateAndTimeStr);

		if((onGoingDate.equals(currentDate) || onGoingDate.before(currentDate)) ){
			//hits alarm
			popUpObject.popUp("It's time for " + eventSubject );
		}



/*
		if(currentDate.after(onGoingDate)){
			System.out.println("this date has alreeady passed doucje");
		}
*/
		TimeUnit.MINUTES.sleep(1);
		System.out.println(oldDateFormat.format(onGoingDate));
		System.out.println(oldDateFormat.format(currentDate));
	}

	private  static void popUpWindow(String tx) throws IOException{
		try {
			final JFrame parent = new JFrame();
	        JButton button = new JButton();
	        JLabel label = new JLabel();

	        label.setText(tx);
	        button.setText("OK");
	        parent.add(button);
	        parent.pack();
	        parent.setVisible(true);

	        button.addActionListener(new java.awt.event.ActionListener() {
	            @Override
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	            	parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
	            }
	        });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
