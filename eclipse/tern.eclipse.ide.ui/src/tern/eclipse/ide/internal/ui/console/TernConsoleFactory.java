package tern.eclipse.ide.internal.ui.console;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleFactory;
import org.eclipse.ui.console.IConsoleManager;

import tern.eclipse.ide.ui.TernUIPlugin;

public class TernConsoleFactory implements IConsoleFactory {

	@Override
	public void openConsole() {
		showConsole();
	}

	public static void showConsole() {
		TernConsole console = (TernConsole) TernUIPlugin.getDefault()
				.getConsole();
		if (console != null) {
			IConsoleManager manager = ConsolePlugin.getDefault()
					.getConsoleManager();
			IConsole[] existing = manager.getConsoles();
			boolean exists = false;
			for (int i = 0; i < existing.length; i++) {
				if (console == existing[i]) {
					exists = true;
				}
			}
			if (!exists) {
				manager.addConsoles(new IConsole[] { console });
			}
			manager.showConsoleView(console);
		}
	}

	public static void closeConsole() {
		IConsoleManager manager = ConsolePlugin.getDefault()
				.getConsoleManager();
		TernConsole console = (TernConsole) TernUIPlugin.getDefault()
				.getConsole();
		if (console != null) {
			manager.removeConsoles(new IConsole[] { console });
			ConsolePlugin.getDefault().getConsoleManager()
					.addConsoleListener(console.new MyLifecycle());
		}
	}

}