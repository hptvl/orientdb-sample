package utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ESMOntNetworkUtil {
	private static final long CIDR_BIT[] = { 0x0, // 0
			0x80000000, // 1
			0xc0000000, // 2
			0xe0000000, // 3
			0xf0000000, // 4
			0xf8000000, // 5
			0xfc000000, // 6
			0xfe000000, // 7
			0xff000000, // 8
			0xff800000, // 9
			0xffc00000, // 10
			0xffe00000, // 11
			0xfff00000, // 12
			0xfff80000, // 13
			0xfffc0000, // 14
			0xfffe0000, // 15
			0xffff0000, // 16
			0xffff8000, // 17
			0xffffc000, // 18
			0xffffe000, // 19
			0xfffff000, // 20
			0xfffff800, // 21
			0xfffffc00, // 22
			0xfffffe00, // 23
			0xffffff00, // 24
			0xffffff80, // 25
			0xffffffc0, // 26
			0xffffffe0, // 27
			0xfffffff0, // 28
			0xfffffff8, // 29
			0xfffffffc, // 30
			0xfffffffe, // 31
			0xffffffff // 32
	};

	public static final long calSIP(long ip, long subnet) {
		return ip & subnet;
	}

	public static final long calEIP(long ip, long subnet) {
		return ip | (~subnet);
	}

	public static final long getRealIP(String ip) {
		InetAddress address = null;

		try {
			address = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			return 0;
		}

		long realIP = address.hashCode();

		if (realIP < 0) {
			realIP ^= 0xFFFFFFFF00000000L;
		}

		return realIP;
	}

	public static final String getRealIP2String(String ip) {
		InetAddress address = null;

		try {
			address = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			return "0";
		}

		long realIP = address.hashCode();

		if (realIP < 0) {
			realIP ^= 0xFFFFFFFF00000000L;
		}

		return String.valueOf(realIP);
	}

	public static final boolean checkIP(String ip) {
		try {
			String regex = "";

			boolean flag = ip.matches(regex);

			if (!flag) {
				return flag;
			}

			long realIP = InetAddress.getByName(ip.trim()).hashCode();

			if (realIP < 0) {
				realIP ^= 0xFFFFFFFF00000000L;

				return flag;
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static final String convertIPToDotted(long realIP) {
		int[] seperatedIP = new int[4];

		seperatedIP[3] = (int) (realIP % 256);
		realIP /= 256;

		seperatedIP[2] = (int) (realIP % 256);
		realIP /= 256;

		seperatedIP[1] = (int) (realIP % 256);
		realIP /= 256;

		seperatedIP[0] = (int) (realIP % 256);
		realIP /= 256;

		return (seperatedIP[0] + "." + seperatedIP[1] + "." + seperatedIP[2] + "." + seperatedIP[3]);
	}

	public static final long convertCIDRToSubnet(int cidrBit) {
		return CIDR_BIT[cidrBit];
	}

	public static final int convertSubnetToCIDR(long subnet) {
		int idx = 0;

		for (long cidr : CIDR_BIT) {
			cidr = (cidr ^= 0xFFFFFFFF00000000L);

			if (cidr == subnet) {
				return idx;
			}

			idx++;
		}

		return 0;
	}

	public static void main(String[] args) throws Exception {
		String srcIP = "27.101.192.213";
		String dstIP = "152.99.26.1";
		System.out.println(getRealIP(srcIP));
		System.out.println(getRealIP(dstIP));
	}
}
