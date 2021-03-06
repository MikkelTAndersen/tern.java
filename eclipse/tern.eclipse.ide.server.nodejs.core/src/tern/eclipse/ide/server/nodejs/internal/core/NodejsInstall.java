package tern.eclipse.ide.server.nodejs.internal.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import tern.eclipse.ide.server.nodejs.core.INodejsInstall;

public class NodejsInstall implements INodejsInstall {

	public static final String NODE_NATIVE = "node-native";

	private final String id;
	private final String name;
	private File path;

	/**
	 * GeneratorType constructor comment.
	 * 
	 * @param element
	 *            a configuration element
	 * @throws IOException
	 */
	public NodejsInstall(IConfigurationElement element) throws IOException {
		this.id = element.getAttribute("id");
		this.name = element.getAttribute("name");
		String pluginId = element.getNamespaceIdentifier();
		String path = element.getAttribute("path");
		if (path != null && path.length() > 0) {
			File baseDir = FileLocator.getBundleFile(Platform
					.getBundle(pluginId));
			this.path = new File(baseDir, path);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public File getPath() {
		return path;
	}

	public void dispose() {

	}

	public boolean isNative() {
		return NODE_NATIVE.equals(getId());
	}
}
