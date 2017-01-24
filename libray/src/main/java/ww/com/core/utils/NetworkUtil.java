package ww.com.core.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkUtil {
	public static boolean isWifi(Context context) {
		boolean reB = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {

			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				reB = true;
			} else {
				int subType = networkInfo.getSubtype();
				switch (subType) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
					reB = false;
					break;
				// ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_CDMA:
					reB = false;
					break;
				// ~ 14-64 kbps
				case TelephonyManager.NETWORK_TYPE_EDGE:
					reB = false;
					break;
				// ~ 50-100 kbps
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
					reB = true; // ~ 400-1000 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
					reB = true; // ~ 600-1400 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_GPRS:
					reB = false;
					break;
				// ~ 100 kbps

				case TelephonyManager.NETWORK_TYPE_HSDPA:
					reB = true; // ~ 2-14 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_HSPA:
					reB = true; // ~ 700-1700 kbps
					break;
				case TelephonyManager.NETWORK_TYPE_HSUPA:
					reB = true; // ~ 1-23 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_UMTS:
					reB = true; // ~ 400-7000 kbps
					break;
				/*
				 * Above API level 7, make sure to set android:targetSdkVersion
				 * to appropriate level to use these
				 */
				case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
					reB = true; // ~ 1-2 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
					reB = true; // ~ 5 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
					reB = true; // ~ 10-20 Mbps
					break;
				case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
					reB = false;
					break;
				// ~25 kbps
				case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
					reB = true; // ~ 10+ Mbps
					break;
				// Unknown
				case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				default:
					reB = false;
					break;
				}
			}
		}

		return reB;
	}
	/**
	 * 功能: WIFI连接状态
	 *
	 * @param context
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static boolean isWiFiActive(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}


	/**
	 * 功能: 网络连接的状态 <br/>
	 *
	 * @param context
	 * @return 作者:fighter <br />
	 *         创建时间:2012-12-16<br />
	 *         修改时间:<br />
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}


}
