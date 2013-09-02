package org.cs27x.dropbox.test;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.cs27x.dropbox.DefaultFileManager;
import org.junit.Test;

public class DefaultFileManagerTest {

	@Test
	public void test() {
		Path p = Paths.get("test");
		DefaultFileManager manager = new DefaultFileManager(p);
		try {
			Files.deleteIfExists(p);
			Files.createFile(p);
		
			// test exist() method
			assertEquals(manager.exists(p), true);

			byte[] content = new String("hello world").getBytes();
			
			// test write() with false override
			manager.write(p, content, false);
			boolean isSame = compareFileContent(content, p);
			assertEquals(isSame, false);
			
			// test write() with true override
			manager.write(p, content, true);
			isSame = compareFileContent(content, p);
			
			assertEquals(isSame, true);
						
			//test delete()
			manager.delete(p);
			assertFalse(manager.exists(p));
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean compareFileContent(byte[] source, Path p) {
		File f = p.toFile();
		byte[] filebyte = new byte[(int) f.length()];
		try {
			DataInputStream is;
			is = new DataInputStream(new FileInputStream(f));
			is.readFully(filebyte);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		System.out.println(filebyte);
//		System.out.println(source);
		
		return new String(source).equals(new String(filebyte));
	}

}
