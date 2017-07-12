package com.incmyapp;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.*;
import javax.swing.event.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;

import com.myappinc.monitor.monitorManager.App;
import com.myappinc.monitor.monitorManager.MonitorJSONWriteFile;


public class Monitor {

	protected Shell shell;
	private Text t_linkAddress;
	protected static Label lblNewLabel;

	/**
	 * Launch the application.
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		try {
			Monitor monitor = new Monitor();
			monitor.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	} 
	
	
	

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
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
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		Button bt_click = new Button(shell, SWT.NONE);
		bt_click.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		bt_click.setBounds(163, 17, 94, 28);
		bt_click.setText("Click");
		
		Label lb_linkAddress = new Label(shell, SWT.NONE);
		lb_linkAddress.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lb_linkAddress.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		lb_linkAddress.setBounds(68, 103, 57, 21);
		lb_linkAddress.setText("Link");
		lb_linkAddress.setVisible(false);
		
		t_linkAddress = new Text(shell, SWT.BORDER);
		t_linkAddress.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 13, SWT.NORMAL));
		t_linkAddress.setBounds(116, 102, 193, 21);
		t_linkAddress.setVisible(false);
		
		Button btn_addLink = new Button(shell, SWT.NONE);
		btn_addLink.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 12, SWT.NORMAL));
		btn_addLink.setBounds(315, 96, 94, 28);
		btn_addLink.setText("Add");
		btn_addLink.setVisible(false);
		
		lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 13, SWT.NORMAL));
		lblNewLabel.setBounds(109, 104, 200, 28);
		lblNewLabel.setVisible(false);
		
		Label lb_red = new Label(shell, SWT.NONE);
		lb_red.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
		lb_red.setBounds(109, 104, 200, 28);
		lb_red.setVisible(false);
		
		Label lblError = new Label(shell, SWT.NONE);
		lblError.setText("");
		lblError.setBounds(163, 160, 94, 28);
		lblError.setVisible(false);
		
		MonitorJSONWriteFile monitorJSONWriteFile = new MonitorJSONWriteFile();
        String linkAddress = "";
		try {
			if(monitorJSONWriteFile != null) {
				linkAddress = monitorJSONWriteFile.readFile();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!linkAddress.trim().equals("")) {
			Monitor monitor = new Monitor();
			boolean statusMonitor = monitor.processMonitor(linkAddress);
			if(statusMonitor == true) {
				lblNewLabel.setBounds(109, 104, 200, 28);
				lblNewLabel.setVisible(true);
				lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
			} else {
				lblNewLabel.setBounds(109, 104, 200, 28);
				lblNewLabel.setVisible(true);
				lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
			}
        }
		
		bt_click.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				bt_click.setVisible(false);
				btn_addLink.setVisible(true);
				lb_linkAddress.setVisible(true);
				t_linkAddress.setVisible(true);
			}
		});
		
		btn_addLink.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
			 lblNewLabel.setText( t_linkAddress.getText());
				
				
				
				 String s = lblNewLabel.toString();
				
				String[] args = null;
				try {
					String linkAddress = t_linkAddress.getText();
					
					if(linkAddress.trim().equals("")) {
						lblError.setText("Link not empty");
						lblError.setVisible(true);
					} else {	
						btn_addLink.setVisible(false);
						lb_linkAddress.setVisible(false);
						t_linkAddress.setVisible(false);
						
						MonitorJSONWriteFile monitorWriteFile = new MonitorJSONWriteFile();
						monitorWriteFile.writeFile(linkAddress);
//						App monitorApp = new App();
//						boolean statusMonitor = monitorApp.runMonitor(linkAddress);
						Monitor monitor = new Monitor();
						boolean statusMonitor = monitor.processMonitor(linkAddress);
						if(statusMonitor == true) {
							lblNewLabel.setVisible(true);
							lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
						} else {
							lblNewLabel.setVisible(true);
							lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
						}
						System.out.println(statusMonitor); 
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
		
			}
		});

	}
	
	private boolean processMonitor(String link) {
		boolean statusMonitor = false;
		try {
			App monitorApp = new App();
			System.out.print(link);
			statusMonitor = monitorApp.runMonitor(link);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return statusMonitor;
	}
}