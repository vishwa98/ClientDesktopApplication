package clientApplication;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class TableCellRedColourRenderer implements TableCellRenderer{
	
	private static final TableCellRenderer RENDERER = new DefaultTableCellRenderer();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		
		Component comp = RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if(column == 5 || column == 4) {  									//checks for CO2 level, Smoke level columns
			Object result = table.getModel().getValueAt(row, column);		//gets the value in the particular cell
			int resultvalue = Integer.parseInt(result.toString());
			Color color = null;
			if(resultvalue > 5) {											
				color = Color.RED;
			}
			
			comp.setForeground(color);									//if the value is greater than 5, set the font color to red
			
		
			
			
		} else {
			comp.setForeground(Color.BLACK);
		}
		
		
		
		return comp;
	}

}
