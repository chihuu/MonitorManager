package com.incmyapp;

import java.awt.Composite;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.jface.viewers.TableViewer;

import javax.swing.Timer;

import com.myappinc.monitor.monitorManager.MonitorJSONWriteFile;

public class TableEx4 {
	private Text text1;
	private Text text2;
	private Table table;
	String url;
	Color color;
	int time = 5000;

	MonitorJSONWriteFile monitorJSONWriteFile = new MonitorJSONWriteFile();
	ArrayList<String> link = new ArrayList<>();
	ArrayList<String> links = new ArrayList<>();
	ArrayList<String> status = new ArrayList<>();
	static Display display = new Display();
	Color red = display.getSystemColor(SWT.COLOR_RED);
	Color green = display.getSystemColor(SWT.COLOR_GREEN);

	public TableEx4(Display display) throws IOException, InterruptedException {

		initUI(display);
	}

	private void initUI(Display display) throws IOException, InterruptedException {

		Shell shell = new Shell(display, SWT.SHELL_TRIM | SWT.CENTER);
		shell.setLayout(new GridLayout(5, false));
		link = monitorJSONWriteFile.readFile();

		table = new Table(shell, SWT.BORDER | SWT.MULTI);
		table.setHeaderVisible(true);

		String[] titles = { "Status", "Url" };

		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[i]);
			column.setWidth(150);

		}

		for (int i = 0; i < link.size(); i++) {
			Monitor monitor = new Monitor();
			String c = null;
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(1, link.get(i));
			boolean statusMonitor = monitor.processMonitor(link.get(i));
			if (statusMonitor == true) {
				c = "Green";
				color = green;
			} else {
				c = "Red";
				color = red;
			}
			System.out.println(c);
			item.setText(0, c);
			item.setForeground(0, color);
			System.out.println("link 1 :" + link);

		}

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 5;
		gd.widthHint = 360;
		gd.heightHint = 300;
		table.setLayoutData(gd);

		Label carName = new Label(shell, SWT.NONE);
		carName.setText("Status:");
		text1 = new Text(shell, SWT.BORDER);
		carName.setVisible(false);
		text1.setVisible(false);

		Label linkAdress = new Label(shell, SWT.NONE);
		linkAdress.setText("Url:");
		text2 = new Text(shell, SWT.BORDER);
		text2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Button addBtn = new Button(shell, SWT.PUSH);
		addBtn.setText("Insert");
		addBtn.addListener(SWT.Selection, event -> {
			try {
				onInsertButtonSelected(event);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		Runnable timer = new Runnable() {
			public void run() {
				try {
					links = monitorJSONWriteFile.readFile();
					link = links;
					// for (int i = 0; i < titles.length; i++) {
					// TableColumn column1 = new TableColumn(table, SWT.NULL);
					// column1.setText(titles[i]);
					// column1.setWidth(150);
					//
					// }
					//
					// for (int i = 0; i < links.size(); i++) {
					// Monitor monitor = new Monitor();
					// String c = null;
					// TableItem item = new TableItem(table, SWT.NULL);
					// item.setText(1, links.get(i));
					// boolean statusMonitor = monitor.processMonitor(links.get(i));
					// if(statusMonitor == true) {
					// c = "Green";
					// color = green;
					// } else {
					// c = "Red";
					// color = red;
					// }
					// System.out.println(c);
					// item.setText(0,c);
					// item.setForeground(0, color);
					//
					//
					//
					//
					// }
					//
					System.out.println("link" + link);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("haha");
				display.timerExec(time, this);
			}
		};
		display.timerExec(time, timer);

		shell.setText("Table widget");
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();

		}

	}

	private void onInsertButtonSelected(Event event) throws IOException {

		String val1 = text1.getText();
		String val2 = text2.getText();
		TableItem item = new TableItem(table, SWT.NULL);

		MonitorJSONWriteFile monitorWriteFile = new MonitorJSONWriteFile();
		monitorWriteFile.writeFile(val2);
		Monitor monitor = new Monitor();
		boolean statusMonitor = monitor.processMonitor(val2);
		if (statusMonitor == true) {
			val1 = " Green";
			color = green;
		} else {
			val1 = "Red";
			color = red;
		}
		item.setText(0, val1);
		item.setText(1, val2);
		item.setForeground(0, color);

	}

	public void restart() throws InterruptedException {

		long endTime = System.currentTimeMillis() + 15000;
		while (System.currentTimeMillis() < endTime) {
			Thread.sleep(endTime);
			System.out.println("restart");
		}

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException, InterruptedException {
		TableEx4 ex = new TableEx4(display);
		display.dispose();
	}
}
