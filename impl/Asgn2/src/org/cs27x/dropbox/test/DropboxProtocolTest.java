package org.cs27x.dropbox.test;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxProtocol;
import org.cs27x.dropbox.DropboxTransport;
import org.cs27x.dropbox.FileManager;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.filewatcher.FileStates;
import org.junit.Test;

public class DropboxProtocolTest {
	private FileStates states = mock(FileStates.class);
	private FileManager mgr = mock(FileManager.class);
	private DropboxTransport trans = mock(DropboxTransport.class);
	private Path p = Paths.get("testProtocol");
	
	@Test
	public void testAddFile() {
		DropboxProtocol protocol = new DropboxProtocol(trans,states,mgr);

		try {
			Files.deleteIfExists(p);
			Files.createFile(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DropboxCmd cmd = new DropboxCmd();
		cmd.setPath(p.getFileName().toString());
		
		cmd.setOpCode(OpCode.ADD);
		protocol.operateFile(p,cmd);		
		verify(trans,times(1)).publish(cmd);
		
	}
	
	@Test
	public void testUpdateFile() {
		DropboxCmd cmd = new DropboxCmd();
		cmd.setPath(p.getFileName().toString());
		DropboxProtocol protocol = new DropboxProtocol(trans,states,mgr);
		
		cmd.setOpCode(OpCode.REMOVE);
		protocol.operateFile(p,cmd);
		verify(trans,times(1)).publish(cmd);
		
	}
	
	@Test
	public void testDeleteFile() {
		DropboxCmd cmd = new DropboxCmd();
		cmd.setPath(p.getFileName().toString());
		DropboxProtocol protocol = new DropboxProtocol(trans,states,mgr);
		
		cmd.setOpCode(OpCode.REMOVE);
		protocol.operateFile(p,cmd);
		verify(trans,times(1)).publish(cmd);
	}


}
