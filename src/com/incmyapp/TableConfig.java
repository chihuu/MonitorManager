package com.incmyapp;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.myappinc.monitor.monitorManager.App;
import com.myappinc.monitor.monitorManager.MonitorJSONWriteFile;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Rectangle;

public class TableConfig {

	protected Shell shell;
	private Text t_ffmpegUrl;
	private Text t_ffprobeUrl;
	ArrayList<String> link = new ArrayList<>();
	Display display = Display.getDefault();
	

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TableConfig window = new TableConfig();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 * @throws IOException 
	 */
	public void open() throws IOException {
		createContents();
		org.eclipse.swt.widgets.Monitor primary = display.getPrimaryMonitor ();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 8;
		int y = bounds.y + (bounds.height - rect.height) / 8;
		shell.setLocation (x, y);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 * @throws IOException 
	 */
	protected void createContents() throws IOException {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("Config");

		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(64, 76, 70, 19);
		lblNewLabel.setText("FFmpeg Url");

		t_ffmpegUrl = new Text(shell, SWT.BORDER);
		t_ffmpegUrl.setBounds(167, 76, 141, 19);

		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(64, 138, 70, 14);
		lblNewLabel_1.setText("FFprobe Url");

		Label lberror2 = new Label(shell, SWT.NONE);
		lberror2.setBounds(314, 133, 22, 14);
		

		Label lberror1 = new Label(shell, SWT.NONE);
		lberror1.setBounds(314, 76, 50, 20);
		lberror1.setText("  ");

		t_ffprobeUrl = new Text(shell, SWT.BORDER);
		t_ffprobeUrl.setBounds(167, 133, 141, 19);
		
		Label lb_sattus = new Label(shell, SWT.NONE);
		lb_sattus.setBounds(314, 107, 100, 14);
		
		String pathFFmpeg;
		String pathFFprobe;
		MonitorJSONWriteFile monitor = new MonitorJSONWriteFile();
		pathFFmpeg = monitor.readConfigFFmpeg();
		pathFFprobe = monitor.readConfigFFmprobe();
		t_ffmpegUrl.setText(pathFFmpeg);
		t_ffprobeUrl.setText(pathFFprobe);

		
		Button btn_insertUrl = new Button(shell, SWT.NONE);
		btn_insertUrl.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String urlFFmpeg = t_ffmpegUrl.getText();
				String urlFFprobe = t_ffprobeUrl.getText();
				MonitorJSONWriteFile monitor = new MonitorJSONWriteFile();
				try {
					if (urlFFmpeg.isEmpty() || urlFFprobe.isEmpty()) {
						Device device = Display.getCurrent();
						Color color = new Color(device, 255, 0, 0);
						lberror1.setText(" * ");
						lberror2.setText(" * ");
						
						//lb_sattus.setText("Please enter a!");
						lberror1.setForeground(color);
						lberror2.setForeground(color);

					} else

						monitor.writeConfigFile(urlFFmpeg.trim(), urlFFprobe.trim());
				} catch (IOException e1) {

					e1.printStackTrace();
				}

			}
		});
		btn_insertUrl.setBounds(167, 195, 94, 28);
		btn_insertUrl.setText("Insert");
		
//		Button btCancel = new Button(shell, SWT.NONE);
//		btCancel.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				
//					display.dispose();
//				
//				
//			}
//		});
//		btCancel.setBounds(67, 195, 94, 28);
//		btCancel.setText("Cancel");

	}
	public void close() {
		display.isDisposed();
	}
}
