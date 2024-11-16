package com.a50tt.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.a50tt.main.DiffHandler;

public class WindowView extends JFrame {

	private JSplitPane splitPane;
	private JPanel panelTop, panelScroll, panelTextPaths, panelStartButton;
	private JTextField textPath1, textPath2;
	private JLabel txtLog;
	private JButton btnStart;
	private JScrollPane scrollPanel;
	private JLabel lblPath1;
	private JLabel lblPath2;
	private JButton btnSwapPaths;
	
	public WindowView() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double resMultiplier = 0.75;
		double width = screenSize.getWidth();
		double widthRatio = width / 1536;
		double height = screenSize.getHeight();
		double heightRatio = height / 864;

		this.setTitle("Path diff checker");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setPreferredSize(new Dimension((int)(width * resMultiplier), (int)(height * resMultiplier)));
		try {
			this.setIconImage(ImageIO.read(new File("res/icon.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerSize(0);
		splitPane.setEnabled(false);

		panelTop = new JPanel();
		panelTop.setBorder(new EmptyBorder(0, 5, 5, 5));
		panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));

		panelTextPaths = new JPanel();
		panelTextPaths.setBorder(new EmptyBorder(5,5,0,5));
		panelTextPaths.setLayout(new GridLayout(4, 1, 0, 8));

		lblPath1 = new JLabel("Main path:");
		lblPath1.setPreferredSize(new Dimension((int)(this.getPreferredSize().getWidth()),(int)(20f * heightRatio)));
		panelTextPaths.add(lblPath1);
		textPath1 = new JTextField("");
		textPath1.setPreferredSize(new Dimension((int)(this.getPreferredSize().getWidth()),(int)(40f * heightRatio)));
		panelTextPaths.add(textPath1);

		lblPath2 = new JLabel("Path to compare with:");
		lblPath2.setPreferredSize(new Dimension((int)(this.getPreferredSize().getWidth()),(int)(20f * heightRatio)));
		textPath2 = new JTextField("");
		textPath2.setPreferredSize(new Dimension((int)(this.getPreferredSize().getWidth()),(int)(40f * heightRatio)));

		panelTextPaths.setPreferredSize(new Dimension(Math.max(textPath1.getPreferredSize().width, textPath2.getPreferredSize().width), lblPath1.getPreferredSize().height + textPath1.getPreferredSize().height + lblPath2.getPreferredSize().height + textPath2.getPreferredSize().height + ((GridLayout)panelTextPaths.getLayout()).getVgap()));

		panelTextPaths.add(lblPath2);
		panelTextPaths.add(textPath2);

		panelStartButton = new JPanel();
		panelStartButton.setBorder(null);
		panelStartButton.setLayout(new GridLayout(1, 2, (int)(150.0 * widthRatio), 0));
		//panelStartButton.setBorder(new EmptyBorder(0, (int)(100 * widthRatio), 0, (int)(100.0 * widthRatio)));
		panelStartButton.setBorder(new EmptyBorder(0, panelTextPaths.getBorder().getBorderInsets(panelTextPaths).right, 0, panelTextPaths.getBorder().getBorderInsets(panelTextPaths).left));
		btnStart = new JButton("DO DIFF");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtLog.setText("<html></html>");
				diffPathsOnBackground(new File(textPath1.getText()), new File(textPath2.getText()), txtLog);
			}
		});
		btnStart.setPreferredSize(new Dimension((int)(150.0 * widthRatio), (int)(30.0 * heightRatio)));
		btnStart.setFont(new Font("Arial Bold", Font.PLAIN, (int)(10.0 + widthRatio)));
		panelStartButton.setPreferredSize(new Dimension((int)(panelTextPaths.getPreferredSize().getWidth()), (int)(btnStart.getPreferredSize().height + ((GridLayout)panelStartButton.getLayout()).getVgap() * 2)));
		
		btnSwapPaths = new JButton("SWAP");
		btnSwapPaths.setFont(new Font("Arial Bold", Font.PLAIN, (int)(10.0 + widthRatio)));
		btnSwapPaths.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String temp = textPath1.getText();
				textPath1.setText(textPath2.getText());
				textPath2.setText(temp);
			}
		});
		btnSwapPaths.setPreferredSize(new Dimension((int)btnStart.getPreferredSize().getWidth(), (int)btnStart.getPreferredSize().getHeight()));
		
		panelStartButton.add(btnSwapPaths);
		panelStartButton.add(btnStart);

		panelTop.add(panelTextPaths);
		panelTop.add(panelStartButton);
		panelTop.setPreferredSize(new Dimension(Math.max(panelTextPaths.getPreferredSize().width, panelStartButton.getPreferredSize().width), panelTextPaths.getPreferredSize().height + panelStartButton.getPreferredSize().height + panelTop.getBorder().getBorderInsets(panelTop).top + panelTop.getBorder().getBorderInsets(panelTop).bottom));

		panelScroll = new JPanel();
		panelScroll.setBorder(new EmptyBorder((int)(5.0 * heightRatio), (int)(5.0 * widthRatio), (int)(5.0 * heightRatio), (int)(5.0 * widthRatio)));
		panelScroll.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		txtLog = new JLabel("<html></html>");
		txtLog.setVerticalAlignment(SwingConstants.TOP);
		panelScroll.add(txtLog);

		scrollPanel = new JScrollPane(panelScroll);
		scrollPanel.getVerticalScrollBar().addAdjustmentListener(
				e -> {
					if ((e.getAdjustable().getValue() - e.getAdjustable().getMaximum()) > - scrollPanel.getHeight() - 20) {
						e.getAdjustable().setValue(e.getAdjustable().getMaximum());
					}
				});
		
		splitPane.add(panelTop);
		splitPane.add(scrollPanel);

		this.getContentPane().add(splitPane);
		this.pack();
		this.update(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);		
	}
	
	public void diffPathsOnBackground(File _mainDir, File _comparingDir, JLabel _textLog) {
		SwingWorker<Void, String> sw = new SwingWorker<Void, String>() {
			protected Void doInBackground() throws Exception {
				DiffHandler.start(_mainDir,  _comparingDir, _textLog);
				return null;
			}
		};
		sw.execute();
	}
}
