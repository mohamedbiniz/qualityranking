import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.db.HelperAcessDB;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ScoreFinalDinamic.java
 *
 * Created on 11/02/2009, 18:58:13
 */
/**
 * 
 * @author Fabricio
 */
public class ScoreFinalDinamic extends javax.swing.JFrame {

	private List<DataSet> dataSets;

	/** Creates new form ScoreFinalDinamic */
	public ScoreFinalDinamic() {
		initComponents();
	}

	private void cleanResults() {
		jTextAreaResults.setText("");
	}

	private void calcularScores() {
		cleanResults();
		DefaultListModel listModel = null;
		if (jListDataSets.getModel() instanceof DefaultListModel) {
			listModel = (DefaultListModel) jListDataSets.getModel();
			String elementDataSet = (String) jListDataSets.getSelectedValue();
			try {
				Long idDataSet = Long
						.parseLong(elementDataSet.split(" ", 2)[0]);
				Integer weightREP = null;
				Integer weightCOM = null;
				Integer weightTIM = null;

				if (!jTextFieldREP.getText().equals("")) {
					weightREP = Integer.parseInt(jTextFieldREP.getText());
				}
				if (!jTextFieldCOM.getText().equals("")) {
					weightCOM = Integer.parseInt(jTextFieldCOM.getText());
				}
				if (!jTextFieldTIM.getText().equals("")) {
					weightTIM = Integer.parseInt(jTextFieldTIM.getText());
				}

				String result = null;
				try {
					result = HelperScoreFinalDinamic.generateScores(idDataSet,
							weightREP, weightCOM, weightTIM, this);
				} catch (Exception e) {
					result = "Poss�vel erro na conex�o com o banco ireval";
					e.printStackTrace();
				}
				jTextAreaResults.setText(result);
			} catch (NullPointerException npe) {
				JOptionPane.showMessageDialog(this, "Selecione um dataSet!",
						"DataSet n�o selecionado", JOptionPane.WARNING_MESSAGE);
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this, "Insira pesos corretos!",
						"Pesos incorretos", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this,
					"Primeiramente � preciso listar os dataSets!",
					"Listar DataSets", JOptionPane.WARNING_MESSAGE);
		}

	}

	private void listarDataSets() {
		DefaultListModel listModel = new DefaultListModel();
		dataSets = HelperAcessDB.findDataSetsAutomaticEvaluated();
		for (DataSet dataSet : dataSets) {
			listModel.addElement(dataSet.toString());
		}
		jListDataSets.setModel(listModel);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextAreaResults = new javax.swing.JTextArea();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jTextFieldCOM = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		jTextFieldREP = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		jTextFieldTIM = new javax.swing.JTextField();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jPanel4 = new javax.swing.JPanel();
		jButtonListarDAtaSets = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jListDataSets = new javax.swing.JList();
		jLabel4 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}
		});
		getContentPane().setLayout(new java.awt.GridLayout());

		jScrollPane1.setMinimumSize(new java.awt.Dimension(5, 5));

		jTextAreaResults.setColumns(20);
		jTextAreaResults.setEditable(false);
		jTextAreaResults.setRows(5);
		jTextAreaResults.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				jTextAreaResultsFocusGained(evt);
			}
		});
		jScrollPane1.setViewportView(jTextAreaResults);

		jLabel1.setText("Weight Completeness:");

		jTextFieldCOM.setText("0");
		jTextFieldCOM.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextFieldCOMActionPerformed(evt);
			}
		});
		jTextFieldCOM.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				jTextFieldCOMFocusGained(evt);
			}
		});

		jLabel2.setText("Weight Reputation:");

		jTextFieldREP.setText("0");
		jTextFieldREP.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextFieldREPActionPerformed(evt);
			}
		});
		jTextFieldREP.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				jTextFieldREPFocusGained(evt);
			}
		});

		jLabel3.setText("Weight Timeliness:");

		jTextFieldTIM.setText("0");
		jTextFieldTIM.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jTextFieldTIMActionPerformed(evt);
			}
		});
		jTextFieldTIM.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				jTextFieldTIMFocusGained(evt);
			}
		});

		jButton1.setText("Calcular Score");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jButton2.setText("Limpar");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(
				jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout
				.setHorizontalGroup(jPanel3Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel3Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel3Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								jLabel2,
																								javax.swing.GroupLayout.Alignment.TRAILING,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								216,
																								Short.MAX_VALUE)
																						.addComponent(
																								jLabel1,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								216,
																								Short.MAX_VALUE)
																						.addComponent(
																								jLabel3,
																								javax.swing.GroupLayout.Alignment.TRAILING,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								216,
																								Short.MAX_VALUE))
																		.addGap(
																				6,
																				6,
																				6))
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addComponent(
																				jButton1,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				216,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addGroup(
																jPanel3Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel3Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								jTextFieldREP)
																						.addComponent(
																								jTextFieldCOM,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								61,
																								Short.MAX_VALUE)
																						.addComponent(
																								jTextFieldTIM,
																								javax.swing.GroupLayout.Alignment.TRAILING,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								109,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addContainerGap())
														.addComponent(
																jButton2,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))));
		jPanel3Layout
				.setVerticalGroup(jPanel3Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel3Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jTextFieldCOM,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel1))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jTextFieldREP,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel2))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jTextFieldTIM,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel3))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel3Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jButton2)
														.addComponent(jButton1))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		jButtonListarDAtaSets
				.setText("Listar DataSets Avaliados Atumaticamente");
		jButtonListarDAtaSets
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jButtonListarDAtaSetsActionPerformed(evt);
					}
				});

		jListDataSets
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jScrollPane2.setViewportView(jListDataSets);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(
				jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jButtonListarDAtaSets, javax.swing.GroupLayout.DEFAULT_SIZE,
				351, Short.MAX_VALUE).addComponent(jScrollPane2,
				javax.swing.GroupLayout.Alignment.TRAILING,
				javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel4Layout.createSequentialGroup().addComponent(
						jButtonListarDAtaSets).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane2,
								javax.swing.GroupLayout.DEFAULT_SIZE, 322,
								Short.MAX_VALUE)));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel2Layout
										.createSequentialGroup()
										.addComponent(
												jPanel4,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jPanel3,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)));

		jLabel4.setText("Score Final:");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jPanel2,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jScrollPane1,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																298,
																Short.MAX_VALUE)
														.addComponent(
																jLabel4,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																298,
																Short.MAX_VALUE))
										.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel4,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				21,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jScrollPane1,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE))
														.addComponent(
																jPanel2,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		getContentPane().add(jPanel1);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jTextFieldCOMActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextFieldCOMActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextFieldCOMActionPerformed

	private void jTextFieldREPActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextFieldREPActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextFieldREPActionPerformed

	private void jTextFieldTIMActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextFieldTIMActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jTextFieldTIMActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		// calcular scores
		calcularScores();

	}// GEN-LAST:event_jButton1ActionPerformed

	private void jButtonListarDAtaSetsActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonListarDAtaSetsActionPerformed
		// listar DataSets
		listarDataSets();
	}// GEN-LAST:event_jButtonListarDAtaSetsActionPerformed

	private void formWindowClosed(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_formWindowClosed
		System.exit(0);
	}// GEN-LAST:event_formWindowClosed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		cleanResults();
	}// GEN-LAST:event_jButton2ActionPerformed

	private void jTextFieldCOMFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldCOMFocusGained
		jTextFieldCOM.selectAll();
	}// GEN-LAST:event_jTextFieldCOMFocusGained

	private void jTextFieldREPFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldREPFocusGained
		jTextFieldREP.selectAll();
	}// GEN-LAST:event_jTextFieldREPFocusGained

	private void jTextFieldTIMFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextFieldTIMFocusGained
		jTextFieldTIM.selectAll();
	}// GEN-LAST:event_jTextFieldTIMFocusGained

	private void jTextAreaResultsFocusGained(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_jTextAreaResultsFocusGained
		jTextAreaResults.selectAll();
	}// GEN-LAST:event_jTextAreaResultsFocusGained

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ScoreFinalDinamic().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButtonListarDAtaSets;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JList jListDataSets;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea jTextAreaResults;
	private javax.swing.JTextField jTextFieldCOM;
	private javax.swing.JTextField jTextFieldREP;
	private javax.swing.JTextField jTextFieldTIM;
	// End of variables declaration//GEN-END:variables
}
