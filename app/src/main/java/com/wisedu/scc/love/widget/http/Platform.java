package com.wisedu.scc.love.widget.http;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Platform {

	private static final String TAG = "Platform";

	private static final String SHARE_PREFERENCES = "platform_config";

    private static final String PLATFORM_TOKEN = "platform_token";
    /**
     * 是否游客
     */
    private static final String PLATFORM_VISITOR = "platform_visitor";
    private static final String PLATFORM_USERNAME = "platform_username";
    private static final String PLATFORM_PASSWORD = "platform_password";
    private static final String PLATFORM_CREDENTIAL_TYPE = "platform_credential_type";

    public static final String API_URL = "apiUrl";

    public static final String FS_URL = "fsUrl";
    public static final String SCC_ST = "SCC-ST";

    private static final String LOGIN_URL = "/oIdentityService?_m=login";

    public final static String ACTION_LOGIN = "scc://login";

    public final static String ACTION_HOME = "scc://home";

    public static final String ACTION_CONSUMMATE = "android.intent.action.CONSUMMATE";

    /**
     * 数据 codeuser(String)
     */
    public static final String ACTION_USER_DETAIL = "android.intent.action.USER_DETAIL";

    public static final String ACTION_GALLERY = "android.intent.action.GALLERY";

    public static final String ACTION_RESET_URL = "android.intent.action.RESET_URL";

    public final static String EXTRA_NOTICE_URL = "APP_OPEN_URI";

    /**
     * 分页
     */
    public static final long LIST_LIMIT = 20L;

	private static Platform platform = null;

    private Context mContext;

	private SharedPreferences mSharedPreferences = null;

    private OnRetrivedPlatformListener mOnRetrivedPlatformListener;

    public static synchronized Platform getPlatform(Context context) {
        if(platform == null) {
            platform = new Platform(context);
        }
        return platform;
    }

	private Platform(Context context) {
        mContext = context.getApplicationContext();
        mSharedPreferences = mContext.getSharedPreferences(
                SHARE_PREFERENCES, Context.MODE_PRIVATE);
	}


    public void setToken(String token) {
        Log.v(TAG, "cc:" + token);
        mSharedPreferences.edit().putString(PLATFORM_TOKEN, token).commit();
    }

    public void setVisitor(boolean isVisitor) {
        Log.v(TAG, "ii:" + isVisitor);
        mSharedPreferences.edit().putBoolean(PLATFORM_VISITOR, isVisitor).commit();
    }

    public String getToken() {
        if(mSharedPreferences.contains(PLATFORM_TOKEN)) {
            return mSharedPreferences.getString(PLATFORM_TOKEN, "");
        }
        if(mOnRetrivedPlatformListener != null) {
            return mOnRetrivedPlatformListener.retrivedToken();
        }
        return "";
    }

    public boolean isVisitor() {
        if(mSharedPreferences.contains(PLATFORM_VISITOR)) {
            return mSharedPreferences.getBoolean(PLATFORM_VISITOR, true);
        }
        if(mOnRetrivedPlatformListener != null) {
            return mOnRetrivedPlatformListener.isVistor();
        }
        return true;
    }

    public void logout() {
        mSharedPreferences.edit().clear().commit();
    }

    public boolean login(HttpManager httpManager) {
        final String username = mSharedPreferences.getString(PLATFORM_USERNAME, "");
        final String password = mSharedPreferences.getString(PLATFORM_PASSWORD, "");
        final int type = mSharedPreferences.getInt(PLATFORM_CREDENTIAL_TYPE, 0);

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
                && TCredentialType.findByValue(type) != null) {
            TCredential credential = new TCredential();
            credential.setName(username);
            credential.setValue(password);
            credential.setType(TCredentialType.findByValue(type));
            Log.v(TAG, "username:" + username);
            Log.v(TAG, "password:" + password);

            Map<String, Object> pararms = new HashMap<String, Object>();
            pararms.put("credential", credential);
            pararms.put("ttl", "-1");

            String ret = HttpManagerWrapper.getInstance(mContext).postSync(LOGIN_URL,
                    pararms);
            Log.v(TAG, "ret:" + ret);

            JSONObject json;
            try {
                json = new JSONObject(ret);
                String code = json.isNull("code") ? "" : json.getString("code");
                String token = json.isNull("ret") ? "" : json.getString("ret");
                if ("0".equals(code)) {
                    setToken(token);
                    setVisitor(false);
                    attachHeader(httpManager, token);
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public void attachHeader(HttpManager httpManager) {
        attachHeader(httpManager, getToken());
    }

    public void attachHeader(HttpManager httpManager, String token) {
        httpManager.addHeader(SCC_ST, token);
    }

    public void startLoginActivity(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ACTION_LOGIN));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setPackage(context.getPackageName());
        context.startActivity(intent);
    }

    public void startHomeActivity(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ACTION_HOME));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setPackage(context.getPackageName());
        context.startActivity(intent);
    }

    public void resetUrl(Context context, String url) {
        Intent intent = new Intent(ACTION_RESET_URL);
        intent.putExtra(EXTRA_NOTICE_URL, url);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    public void recordLoginMsg(String username, String password, int credentialType) {
        mSharedPreferences.edit().putString(PLATFORM_USERNAME, username)
                .putString(PLATFORM_PASSWORD, password).putInt(PLATFORM_CREDENTIAL_TYPE, credentialType)
                .commit();
    }

    public void setOnRetrivedPlatformListener(OnRetrivedPlatformListener onRetrivedPlatformListener) {
        this.mOnRetrivedPlatformListener = onRetrivedPlatformListener;
    }

    public OnRetrivedPlatformListener getOnRetrivedPlatformListener() {
        return mOnRetrivedPlatformListener;
    }

    public interface OnRetrivedPlatformListener {
        public String retrivedToken();

        /**
         * 是否游客
         * @return 游客
         */
        public boolean isVistor();
    }

    /**
     * 凭证
     */
    public class TCredential implements java.io.Serializable, Cloneable {
        private static final long serialVersionUID = 1L;

        private long id; // optional
        private long accountId; // optional
        private TCredentialType type; // optional
        private String name; // optional
        private String value; // optional
        private Map<String, String> attributes; // optional

        /**
         * 标识
         */
        public long getId() {
            return this.id;
        }

        /**
         * 标识
         */
        public void setId(long id) {
            this.id = id;
        }

        /**
         * 账户标识
         */
        public long getAccountId() {
            return this.accountId;
        }

        /**
         * 账户标识
         */
        public void setAccountId(long accountId) {
            this.accountId = accountId;
        }

        /**
         * 凭证类型
         *
         * @see TCredentialType
         */
        public TCredentialType getType() {
            return this.type;
        }

        /**
         * 凭证类型
         *
         * @see TCredentialType
         */
        public void setType(TCredentialType type) {
            this.type = type;
        }

        /**
         * 凭证名：如邮件地址、手机号等
         */
        public String getName() {
            return this.name;
        }

        /**
         * 凭证名：如邮件地址、手机号等
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 凭证值：登录时为帐户密码，绑定认证时为认证码
         */
        public String getValue() {
            return this.value;
        }

        /**
         * 凭证值：登录时为帐户密码，绑定认证时为认证码
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * 扩展属性
         */
        public Map<String, String> getAttributes() {
            return this.attributes;
        }

        /**
         * 扩展属性
         */
        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }
    }

    /**
     * 凭证类型/帐户绑定
     */
    public enum TCredentialType {
        /**
         * 普通用户名/学号/工号
         */
        USERNAME(0),
        /**
         * 电子邮箱
         */
        EMAIL(1),
        /**
         * 手机号
         */
        MOBILE(2),
        /**
         * 新浪微博
         */
        SINA(3),
        /**
         * 腾讯微博
         */
        TENCENT(4),
        /**
         * 人人网
         */
        RENREN(5),
        /**
         * 学校统一身份认证
         */
        IDS(6),
        /**
         * 其他
         */
        OTHER(7),
        /**
         * 移动社区平台（客户端认证）
         */
        SNC(8),
        /**
         * IDS5登录
         */
        IDS5(9),
        /**
         * OAUTH登录
         */
        OAUTH(10),
        /**
         * CAS登录
         */
        CAS(11),
        /**
         * 移动社区平台（服务端认证）
         */
        SNC_SERVER(12),
        /**
         * QQ空间
         */
        QQ(13),
        /**
         * WEB版IDS
         */
        IDSWEB(14);

        private final int value;

        private TCredentialType(int value) {
            this.value = value;
        }

        /**
         * Get the integer value of this enum value, as defined in the Thrift
         * IDL.
         */
        public int getValue() {
            return value;
        }

        /**
         * Find a the enum type by its integer value, as defined in the Thrift
         * IDL.
         *
         * @return null if the value is not found.
         */
        public static TCredentialType findByValue(int value) {
            switch (value) {
                case 0:
                    return USERNAME;
                case 1:
                    return EMAIL;
                case 2:
                    return MOBILE;
                case 3:
                    return SINA;
                case 4:
                    return TENCENT;
                case 5:
                    return RENREN;
                case 6:
                    return IDS;
                case 7:
                    return OTHER;
                case 8:
                    return SNC;
                case 9:
                    return IDS5;
                case 10:
                    return OAUTH;
                case 11:
                    return CAS;
                case 12:
                    return SNC_SERVER;
                case 13:
                    return QQ;
                case 14:
                    return IDSWEB;
                default:
                    return null;
            }
        }
    }
}
