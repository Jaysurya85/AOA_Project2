
import java.util.*;

class VM {
	int id;
	int size;

	public VM(int id, int size) {
		this.id = id;
		this.size = size;
	}

	@Override
	public String toString() {
		return "VM(id=" + id + ", size=" + size + ")";
	}
}

class Server {
	int id;
	int capacity;
	int used;
	List<VM> vms;

	public Server(int id, int capacity) {
		this.id = id;
		this.capacity = capacity;
		this.used = 0;
		this.vms = new ArrayList<>();
	}

	public boolean canFit(VM vm) {
		return used + vm.size <= capacity;
	}

	public void addVM(VM vm) {
		used += vm.size;
		vms.add(vm);
	}

	@Override
	public String toString() {
		return "Server(id=" + id + ", used=" + used + "/" + capacity + ", VMs=" + vms + ")";
	}
}

class VMScheduler {

	public List<Server> placeVMsFFD(List<VM> vms, int serverCapacity) {
		vms.sort((a, b) -> b.size - a.size);

		List<Server> servers = new ArrayList<>();
		int serverCounter = 1;
		for (VM vm : vms) {
			boolean placed = false;

			for (Server server : servers) {
				if (server.canFit(vm)) {
					server.addVM(vm);
					placed = true;
					break;
				}
			}

			if (!placed) {
				Server newServer = new Server(serverCounter++, serverCapacity);
				newServer.addVM(vm);
				servers.add(newServer);
			}
		}

		return servers;
	}
}

public class NPHard {
	public static void main(String[] args) {

		int[] sizes = { 8, 5, 6, 3, 2, 4, 7 };

		List<VM> vms = new ArrayList<>();
		for (int i = 0; i < sizes.length; i++) {
			vms.add(new VM(i + 1, sizes[i]));
		}

		int serverCapacity = 10;
		VMScheduler scheduler = new VMScheduler();

		List<Server> solution = scheduler.placeVMsFFD(vms, serverCapacity);

		System.out.println("=== VM Placement Using First-Fit Decreasing ===");
		for (Server s : solution) {
			System.out.println(s);
		}

		System.out.println("\nTotal Servers Used: " + solution.size());
	}
}
