package com.sfb805.nx;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import nxopen.NXException;
import nxopen.NXRemotableObject;
import nxopen.Session;
import nxopen.SessionFactory;
import nxopen.UI;
import nxopen.uf.UF;

public final class RemoteSessionProvider {

	private static RemoteSessionProvider uniqueInstance = new RemoteSessionProvider();

	public static RemoteSessionProvider getInstance() {
		return RemoteSessionProvider.uniqueInstance;
	}

	public static void main(final String[] args) throws RemoteException, NXException {
		RemoteSessionProvider.startup(args);
	}

	public static void startup(final String[] args) throws NXException, java.rmi.RemoteException {
		RemoteSessionProvider.getInstance().registerServer();
	}

	private RemoteSessionProvider() {
	}

	private void registerServer() {
		try {
			LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		} catch (final RemoteException e) {
		}
		try {
			
			//RMI teil für NX direkt. Registriert NX Session,  NX gui und NX UF in der registry-registry 
			
			final Session theSession = (Session) SessionFactory.get("Session", NXRemotableObject.RemotingProtocol.create());
			final UI theUI = (UI) SessionFactory.get("UI", NXRemotableObject.RemotingProtocol.create());
			final UF theUF = (UF) SessionFactory.get("UFSession", NXRemotableObject.RemotingProtocol.create());
			final Registry registry = LocateRegistry.getRegistry();
			registry.rebind("NXSession", theSession);
			registry.rebind("NXgui", theUI);
			registry.rebind("NXUFSession", theUF);
		} catch (final RemoteException e) {
		} catch (final NXException e) {
		}
	}

}