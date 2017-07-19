package com.incmyapp;

import java.awt.Composite;
import java.awt.Point;
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
	int time = 5 * 60 * 1000;
	int indexs;
	String[] titles = { "Name", "Url" };

	MonitorJSONWriteFile monitorJSONWriteFile = new MonitorJSONWriteFile();
	ArrayList<String> link = new ArrayList<>();
	ArrayList<String> linksname = new ArrayList<>();

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
		this.run();

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 5;
		gd.widthHint = 360;
		gd.heightHint = 300;
		table.setLayoutData(gd);

		Label carName = new Label(shell, SWT.NONE);
		carName.setText("Name :");
		text1 = new Text(shell, SWT.BORDER);
		carName.setVisible(true);
		text1.setVisible(true);

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

		Button deleteBtn = new Button(shell, SWT.PUSH);
		deleteBtn.setText("Delete");
		deleteBtn.addListener(SWT.Selection, event -> {
			indexs = table.getSelectionIndex();
			TableItem[] tableItems = table.getItems();
			tableItems[indexs].getText();
			table.remove(table.getSelectionIndex());
			try {
				monitorJSONWriteFile.deleteJson(indexs);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		this.restart();
		shell.setText("Monitor Manager");
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
		monitorWriteFile.writeFile(val1, val2);
		Monitor monitor = new Monitor();
		boolean statusMonitor = monitor.processMonitor(val2);
		if (statusMonitor == true) {
			color = green;
		} else {
			color = red;
		}
		item.setText(0, val1);
		item.setText(1, val2);
		item.setForeground(1, color);

	}

	public void run() throws IOException {
		linksname = monitorJSONWriteFile.readName();
		
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[i]);
			column.setWidth(150);

		}

		for (int i = 0; i < link.size(); i++) {
			Monitor monitor = new Monitor();

			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(1, link.get(i));

			boolean statusMonitor = monitor.processMonitor(link.get(i).trim());
			if (statusMonitor == true) {

				color = green;
			} else {

				color = red;
			}
			
			item.setText(0, linksname.get(i));
			item.setForeground(1, color);

		}

	}

	public void restart() throws InterruptedException {

		Runnable timer = new Runnable() {
			public void run() {
				try {
					link = monitorJSONWriteFile.readFile();
					linksname = monitorJSONWriteFile.readName();

					table.setItemCount(0);
					for (int i = 0; i < link.size(); i++) {

						Monitor monitor = new Monitor();

						TableItem item = new TableItem(table, SWT.NULL);

						System.out.println(table.getItemCount());
						item.setText(1, link.get(i));
						boolean statusMonitor = monitor.processMonitor(link.get(i));
						if (statusMonitor == true) {
							color = green;
						} else {
							color = red;
						}
						System.out.println(linksname.get(i));
						item.setText(0, linksname.get(i));
						item.setForeground(1, color);

					}

					System.out.println("link" + link);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("reload");
				display.timerExec(time, this);
			}
		};
		display.timerExec(time, timer);

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException, InterruptedException {
		TableEx4 ex = new TableEx4(display);
		display.dispose();
	}
}
