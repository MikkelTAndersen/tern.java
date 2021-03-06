package tern.eclipse.ide.server.rhino.internal.core;

import tern.TernProject;
import tern.eclipse.ide.core.ITernServerFactory;
import tern.server.ITernServer;
import tern.server.rhino.RhinoTernServer;

public class TernRhinoServerFactory implements ITernServerFactory {

	@Override
	public ITernServer create(TernProject project) throws Exception {
		return new RhinoTernServer(project);
	}

}
