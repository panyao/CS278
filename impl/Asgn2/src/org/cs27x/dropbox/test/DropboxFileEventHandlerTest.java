package org.cs27x.dropbox.test;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import org.cs27x.dropbox.DropboxCmd;
import org.cs27x.dropbox.DropboxCmd.OpCode;
import org.cs27x.dropbox.DropboxProtocol;
import org.cs27x.dropbox.FileManager;
import org.cs27x.filewatcher.DropboxFileEventHandler;
import org.cs27x.filewatcher.FileEvent;
import org.cs27x.filewatcher.FileState;
import org.cs27x.filewatcher.FileStates;
import org.junit.Test;

public class DropboxFileEventHandlerTest {

	@Test
	public void test() {

		FileStates states = mock(FileStates.class);
		FileManager mgr = mock(FileManager.class);
		DropboxProtocol protocol = mock(DropboxProtocol.class);

		DropboxFileEventHandler handler = new DropboxFileEventHandler(mgr, states, protocol);
		Path p = Paths.get("foo");
		
		
		when(mgr.resolve(any(String.class))).thenReturn(p);
		FileEvent evt = new FileEvent(ENTRY_CREATE, p);
		
		try {
			when(states.filter(any(FileEvent.class))).thenReturn(evt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		handler.handle(evt);
		verify(protocol,times(1)).addFile(p);
		
		evt = new FileEvent(ENTRY_MODIFY, p);
		try {
			when(states.filter(any(FileEvent.class))).thenReturn(evt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.handle(evt);
		verify(protocol,times(1)).updateFile(p);
		
		evt = new FileEvent(ENTRY_DELETE, p);
		try {
			when(states.filter(any(FileEvent.class))).thenReturn(evt);
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.handle(evt);
		verify(protocol,times(1)).removeFile(p);
	}

}
