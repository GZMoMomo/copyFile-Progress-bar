import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class main{
     static long srcFileTollay=0;
     static long copyFileTollay=0;
     static JFrame f=new JFrame("lol");
     static JButton b=new JButton("开始复制");
     static JProgressBar pb=new JProgressBar();
    public static void main(String[] args) {
    	
    	f.setSize(450, 200);
    	f.setLocation(200,200);
    	f.setLayout(null);
    	
    	JPanel pInput=new JPanel();
    	pInput.setLayout(null);
        pInput.setBounds(0,0,380,200);
        
        JLabel lsrcPath=new JLabel("源文件地址:");lsrcPath.setBounds(55,20,70,30);
        JTextField tfSrcPath=new JTextField("");tfSrcPath.setBounds(135, 20, 70, 30);
        JLabel ldestPath=new JLabel("复制到：");ldestPath.setBounds(240, 20, 60, 30);
        JTextField tfdestPath=new JTextField("");tfdestPath.setBounds(300, 20, 70, 30);
        pInput.add(lsrcPath);pInput.add(tfSrcPath);
        pInput.add(ldestPath);pInput.add(tfdestPath);
        
      
        b.setBounds(55,80,100, 50);
        pInput.add(b);
        
        JLabel lProgress=new JLabel("文件复制进度:");
        lProgress.setBounds(160, 90, 100, 30);
        pInput.add(lProgress);
        
        
        pb.setBounds(260, 90, 120, 30);
        pb.setMaximum(100);
        pb.setStringPainted(true);
        pInput.add(pb);
        
        f.add(pInput);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String check=checkSrcPath(tfSrcPath);
				if(check==null)
					return;
				String check2=checkDestPath(tfdestPath);
				if(check2==null)
					return;
				
				String srcPath=tfSrcPath.getText();
				String destPath=tfdestPath.getText();
				srcFileTollay=getLength(srcPath,destPath);
				
				copyFolder(srcPath,destPath);
				
				
			}
		});
        
    }
    
    public static long getLength(String srcPath,String destPath) {
    	File srcFile=new File(srcPath);
    	File destFile=new File(destPath);
    	File[] files=srcFile.listFiles();
    	for(File eachFile:files) {
    		if(eachFile.isFile()) {
    			srcFileTollay+=eachFile.length();
    		}
    		if(eachFile.isDirectory()) {
    			File newDestFile=new File(destFile,eachFile.getName());
    			getLength(eachFile.getAbsolutePath(),newDestFile.getAbsolutePath());
    		}
    	}
    	return srcFileTollay;
    }
    
    public static void copyFolder(String srcFilePath,String destFilePath) {
    	File srcFile=new File(srcFilePath);
    	File destFile=new File(destFilePath);
    	if(!srcFile.exists())
    		return;
    	if(srcFile.isFile())
    		return;
    	if(!srcFile.isDirectory())
    		return;
    	if(!destFile.exists())
    		destFile.mkdirs();
    	File[] files=srcFile.listFiles();
    	for(File eachFile:files) {
    		if(eachFile.isFile()) {
    			File destCopyFile=new File(destFile,eachFile.getName());
    			copyFileTollay+=eachFile.length();
    			copyFile(eachFile.getAbsolutePath(),destCopyFile.getAbsolutePath());
    			Thread t1=new Thread() {
    	        	public void run() {
    	        		double rate=(double)copyFileTollay/(double)srcFileTollay;
    	        		int intRate=(int)(rate*100);
    	        		pb.setValue(intRate);
    	        		pb.setStringPainted(true);
    	        		if(intRate>=100) {
    	        			JOptionPane.showMessageDialog(f, "复制完成");
    	        			b.setEnabled(true);
    	        			
    	        		}
    	        		
    	        			
    	        	}
    	        };
    	        t1.start();
    		}
    		if(eachFile.isDirectory()) {
    			File destCopyFile=new File(destFile,eachFile.getName());
    			copyFolder(eachFile.getAbsolutePath(),destCopyFile.getAbsolutePath());
    		}
    	}
    }
    
    
    public static void copyFile(String srcFilePath,String destFilePath) {
    	File srcFile=new File(srcFilePath);
    	File destFile=new File(destFilePath);
    	byte[] buffer=new byte[1024];
    	
    	try(FileInputStream fis=new FileInputStream(srcFile);
    		FileOutputStream fos=new FileOutputStream(destFile);	
    			) {
    		while(true) {
    			int actullyLength=fis.read(buffer);
    			if(fis.read(buffer)==-1) {
    				break;
    			}
    			fos.write(buffer, 0, actullyLength);
    			fos.flush();
    		}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    public static String checkSrcPath(JTextField jt) {
    	if(jt.getText().length()==0) {
    		JOptionPane.showMessageDialog(f, "源文件地址不能为空");
    		jt.grabFocus();
    		return null;
    	}
    	File checkFile=new File(jt.getText());
    	if(!checkFile.exists()) {
    		JOptionPane.showMessageDialog(f, "文件不存在");
    		jt.grabFocus();
    		return null;
    	}
    	return "";
    }
    
    public static String checkDestPath(JTextField jt) {
    	if(jt.getText().length()==0) {
    		JOptionPane.showMessageDialog(f, "目标地址不能为空");
    		jt.grabFocus();
    		return null;
    	}
    	File checkFile=new File(jt.getText());
    	if(!checkFile.exists()) {
    		checkFile.mkdirs();
    		return "";
    	}
    	return "";
    }
   }
        
