package tern.server;

import java.util.ArrayList;
import java.util.List;

import tern.TernFileManager;
import tern.TernProject;
import tern.server.protocol.completions.ITernCompletionCollector;

public abstract class AbstractTernServer implements ITernServer {

	private final TernProject<?> project;

	private final List<ITernServerListener> listeners;

	private boolean dataAsJsonString;
	private boolean dispose;

	public AbstractTernServer(TernProject<?> project) {
		this.project = project;
		this.listeners = new ArrayList<ITernServerListener>();
		final TernFileManager<?> fileManager = getFileManager();
		if (fileManager != null) {
			this.addServerListener(new TernServerAdapter() {
				@Override
				public void onStop(ITernServer server) {
					fileManager.cleanIndexedFiles();
				}
			});
		}
	}

	public boolean isDataAsJsonString() {
		return dataAsJsonString;
	}

	public void setDataAsJsonString(boolean dataAsJsonString) {
		this.dataAsJsonString = dataAsJsonString;
	}

	@Override
	public void addServerListener(ITernServerListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeServerListener(ITernServerListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	protected void fireStartServer() {
		synchronized (listeners) {
			for (ITernServerListener listener : listeners) {
				listener.onStart(this);
			}
		}
	}

	protected void fireEndServer() {
		synchronized (listeners) {
			for (ITernServerListener listener : listeners) {
				listener.onStop(this);
			}
		}
	}

	@Override
	public final void dispose() {
		if (!isDisposed()) {
			this.dispose = true;
			doDispose();
			fireEndServer();
		}
	}

	@Override
	public boolean isDisposed() {
		return dispose;
	}

	protected abstract void doDispose();

	protected void addProposal(Object completion, int pos,
			ITernCompletionCollector collector) {
		String name = getText(completion, "name");
		String type = getText(completion, "type");
		String origin = getText(completion, "origin");
		Object doc = getText(completion, "doc");
		collector.addProposal(name, type, origin, doc, pos, completion);
	}

	public String getText(Object value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

	public String getText(Object value, String name) {
		return getText(getValue(value, name));
	}

	public abstract Object getValue(Object value, String name);

	@Override
	public TernFileManager<?> getFileManager() {
		if (project != null) {
			return project.getFileManager();
		}
		return null;
	}

	public TernProject<?> getProject() {
		return project;
	}
}
