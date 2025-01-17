/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on <mihail@breadwallet.com> 8/3/17.
 * Copyright (c) 2017 breadwallet LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.breadwallet.tools.util;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import com.breadwallet.appcore.BuildConfig;
import com.breadwallet.tools.manager.BRReportsManager;
import com.platform.APIClient;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class EventUtils {
    public static final String EVENT_REACHABLE = "reachability.isReachble";
    public static final String EVENT_NOT_REACHABLE = "reachability.isNotReachable";
    public static final String EVENT_WALLET_DID_USE_DEFAULT_FEE_PER_KB = "wallet.didUseDefaultFeePerKB";
    public static final String EVENT_PROMPT_PREFIX = "prompt.";
    public static final String EVENT_PROMPT_SUFFIX_TRIGGER = ".trigger";
    public static final String EVENT_PROMPT_SUFFIX_DISPLAYED = ".displayed";
    public static final String EVENT_PROMPT_SUFFIX_DISMISSED = ".dismissed";
    public static final String EVENT_SEND_HANDLE_URL = "send.handleURL";
    public static final String EVENT_LOGIN_SUCCESS = "login.success";
    public static final String EVENT_LOGIN_FAILED = "login.failed";
    public static final String EVENT_LOGIN_LOCKED = "login.locked";
    public static final String EVENT_LOGIN_UNLOCKED = "login.unlocked";
    public static final String EVENT_AMOUNT_SWAP_CURRENCY = "amount.swapCurrency";
    public static final String EVENT_SEND_SUCCESS = "send.success";
    public static final String EVENT_SEND_PUBLISH_FAILED = "send.publishFailed";
    public static final String EVENT_RECEIVE_COPIED_ADDRESS = "receive.copiedAddress";
    //todo implement the rest in DROID-845
    public static final String EVENT_RECEIVE_EMAIL_UNAVAILABLE = "receive.emailUnavailable";
    public static final String EVENT_RECEIVE_PRESENT_MAIL_COMPOSE = "receive.presentMailCompose";
    public static final String EVENT_RECEIVE_MESSAGING_UNAVAILABLE = "receive.messagingUnavailable";
    public static final String EVENT_RECEIVE_PRESENT_MESSAGE = "receive.presentMessage";
    public static final String EVENT_ENTER_PHRASE_SET_SEED = "enterPhrase.setSeed";
    public static final String EVENT_ENTER_PHRASE_RESET_PIN = "enterPhrase.resettingPin";
    public static final String EVENT_ENTER_PHRASE_WIPE_WALLET = "enterPhrase.wipeWallet";
    public static final String EVENT_ENTER_PHRASE_VALID = "enterPhrase.valid";
    public static final String EVENT_ENTER_PHRASE_INVALID = "enterPhrase.invalid";
    public static final String EVENT_SCAN_CAMERA_DENIED = "scan.cameraDenied";
    public static final String EVENT_SCAN_TORCH_ON = "scan.torchOn";
    public static final String EVENT_SCAN_TORCH_OFF = "scan.torchOff";
    public static final String EVENT_SCAN_DISMISS = "scan.dismiss";
    public static final String EVENT_SCAN_BITCOIN_URI = "scan.bitcoinUri";
    public static final String EVENT_SCAN_BCASH_ADDRESS = "scan.bCashAddr";
    public static final String EVENT_SCAN_ETH_ADDRESS = "scan.ethAddress";
    public static final String EVENT_SCAN_PRIVATE_KEY = "scan.privateKey";
    public static final String EVENT_SCAN_DEEP_LINK = "scan.deepLink";
    public static final String EVENT_SCAN_PAIRING_REQUEST = "scan.pairingRequest";
    public static final String EVENT_MAX_DIGITS_SET = "maxDigits.set";
    public static final String EVENT_RECOMMENDED_RESCAN = "event.recommendRescan";
    public static final String EVENT_SET_DEFAULT_CURRENCY = "event.setDefaultCurrency";
    public static final String EVENT_ENABLE_BIOMETRICS = "event.enableBiometrics";
    public static final String EVENT_IS_BTC_SWAPPED = "event.isBTCSwapped";
    public static final String EVENT_SYNC_ERROR_MESSAGE = "event.syncErrorMessage";
    public static final String EVENT_HOME_DID_TAP_BUY = "home.didTapBuy";
    public static final String EVENT_CURRENCY_DID_TAP_SELL_BITCOIN = "currency.didTapSellBitcoin";
    public static final String EVENT_CURRENCY_DID_TAP_TRADE = "currency.didTapTrade";
    public static final String EVENT_PUSH_FIRST_PROMPT_DENIED = "push.firstPromptDenied";
    public static final String EVENT_PUSH_SYSTEM_PROMPT_DENIED = "push.systemPromptDenied";
    public static final String EVENT_PUSH_ENABLE_SETTINGS = "push.enabledSettings";
    public static final String EVENT_PUSH_DISABLE_SETTINGS = "push.disabledSettings";
    public static final String EVENT_BACKGROUND = "background";
    public static final String EVENT_FOREGROUND = "foreground";
    public static final String EVENT_BRD_PAGE_NAVIGATED = "brdcom-page-nav";
    public static final String EVENT_NODE_SELECTOR_SWITCH_TO_AUTO = "nodeSelector.switchToAuto";
    public static final String EVENT_NODE_SELECTOR_SWITCH_TO_MANUAL = "nodeSelector.switchToManual";
    public static final String EVENT_LANDING_PAGE_APPEARED = "onboarding.landingPage";
    public static final String EVENT_LANDING_PAGE_GET_STARTED = "onboarding.landingPage.getStartedButton";
    public static final String EVENT_LANDING_PAGE_RESTORE_WALLET = "onboarding.landingPage.restoreWalletButton";
    public static final String EVENT_GLOBE_PAGE_APPEARED = "onboarding.globePage";
    public static final String EVENT_GLOBE_PAGE_NEXT = "onboarding.globePage.nextButton"; //not needed for android for now
    public static final String EVENT_COINS_PAGE_APPEARED = "onboarding.coinsPage";
    public static final String EVENT_COINS_PAGE_NEXT = "onboarding.coinsPage.nextButton"; //not needed for android for now
    public static final String EVENT_FINAL_PAGE_APPEARED = "onboarding.finalPage";
    public static final String EVENT_FINAL_PAGE_BUY_COIN = "onboarding.finalPage.buyCoin";
    public static final String EVENT_FINAL_PAGE_BROWSE_FIRST = "onboarding.finalPage.browseFirst";
    public static final String EVENT_SKIP_BUTTON = "onboarding.skipButton";
    public static final String EVENT_BACK_BUTTON = "onboarding.backButton";
    public static final String EVENT_ONBOARDING_PIN_CREATED = "onboarding.setPin.pinCreated";
    public static final String EVENT_PAPER_KEY_INTRO_APPEARED = "onboarding.paperKeyIntro.appeared";
    public static final String EVENT_PAPER_KEY_INTRO_DISMISSED = "onboarding.paperKeyIntro.dismissed";
    public static final String EVENT_PAPER_KEY_INTRO_GENEREATE_KEY = "onboarding.paperKeyIntro.generatePaperKeyButton";
    public static final String EVENT_ONBOARDING_COMPLETE = "onboarding.complete";
    public static final String EVENT_REWARDS_OPEN_WALLET = "rewards.openwallet";
    public static final String EVENT_REWARDS_BANNER = "rewards.banner";
    public static final String EVENT_ATTRIBUTE_CURRENCY = "currency";
    public static final String EVENT_PUB_KEY_MISMATCH = "publicKeyMismatch";
    public static final String EVENT_ATTRIBUTE_REWARDS_ID_HASH = "rewards_id_hash";
    public static final String EVENT_ATTRIBUTE_ADDRESS_HASH = "address_hash";
    // In-app messages
    public static final String EVENT_IN_APP_NOTIFICATION_RECEIVED = "inAppNotifications.receivedNotification";
    public static final String EVENT_IN_APP_NOTIFICATION_APPEARED = "inAppNotifications.inAppNotification.appeared";
    public static final String EVENT_IN_APP_NOTIFICATION_DISMISSED = "inAppNotifications.inAppNotification.dismissed";
    public static final String EVENT_IN_APP_NOTIFICATION_CTA_BUTTON = "inAppNotifications.inAppNotification.notificationCTAButton";
    public static final String EVENT_ATTRIBUTE_NOTIFICATION_ID = "id";
    public static final String EVENT_ATTRIBUTE_NOTIFICATION_CTA_URL = "cta_url";
    public static final String EVENT_ATTRIBUTE_MESSAGE_ID = "message_id";
    // Google Play review prompt
    public static final String EVENT_REVIEW_PROMPT_DISPLAYED = "prompt.playstoreRate.displayed";
    public static final String EVENT_REVIEW_PROMPT_GOOGLE_PLAY_TRIGGERED = "prompt.playstoreRate.trigger";
    public static final String EVENT_REVIEW_PROMPT_DISMISSED = "prompt.playstoreRate.dismissed";
    // Push notifications
    public static final String EVENT_PUSH_NOTIFICATIONS_OPEN_APP_SETTINGS = "pushNotifications.pushNotificationSettings.appeared";
    public static final String EVENT_PUSH_NOTIFICATIONS_SETTING_TOGGLE_ON = "pushNotifications.pushNotificationSettings.pushNotificationsToggleOn";
    public static final String EVENT_PUSH_NOTIFICATIONS_SETTING_TOGGLE_OFF = "pushNotifications.pushNotificationSettings.pushNotificationsToggleOff";
    public static final String EVENT_PUSH_NOTIFICATIONS_OPEN_OS_SETTING = "pushNotifications.pushNotificationSettings.openNotificationSystemSettings";
    public static final String EVENT_PUSH_NOTIFICATION_OPEN = "pushNotifications.openNotification";
    public static final String EVENT_PUSH_NOTIFICATION_RECEIVED = "pushNotifications.receivedNotification";
    // Used for campaign analysis
    public static final String EVENT_MIXPANEL_APP_OPEN = "$app_open";
    public static final String EVENT_ATTRIBUTE_CAMPAIGN_ID = "campaign_id";
    // Experiment events
    public static final String EVENT_EXPERIMENT_BUY_SELL_MENU_BUTTON = "experiment.buySellMenuButton";
    public static final String EVENT_ATTRIBUTE_BUY_AND_SELL = "buyAndSell";
    public static final String EVENT_ATTRIBUTE_SHOW = "show";
    // Wallet events
    // wallet.{currencyCode}.appeared
    public static final String EVENT_WALLET_APPEARED = "wallet.%s.appeared";
    // wallet.{currencyCode}.axisToggle
    public static final String EVENT_WALLET_CHART_AXIS_TOGGLE = "wallet.%s.axisToggle";
    // wallet.{currencyCode}.scrubbed
    public static final String EVENT_WALLET_CHART_SCRUBBED = "wallet.%s.scrubbed";
    // Home screen prompts events
    public static final String PROMPT_TOUCH_ID = EventUtils.EVENT_PROMPT_PREFIX + "touchIdPrompt";
    public static final String PROMPT_PAPER_KEY = EventUtils.EVENT_PROMPT_PREFIX + "paperKeyPrompt";
    public static final String PROMPT_UPGRADE_PIN = EventUtils.EVENT_PROMPT_PREFIX + "upgradePinPrompt";
    public static final String PROMPT_RECOMMEND_RESCAN = EventUtils.EVENT_PROMPT_PREFIX + "recommendRescanPrompt";
    public static final String PROMPT_EMAIL = EventUtils.EVENT_PROMPT_PREFIX + "emailPrompt";
    public static final String PROMPT_RATE_APP = EventUtils.EVENT_PROMPT_PREFIX + "rateAppPrompt";
    // Gift events
    public static final String EVENT_GIFT_SEND = "gift.send";
    public static final String EVENT_GIFT_REDEEM = "gift.redeem";
    public static final String EVENT_GIFT_REDEEM_LINK = "gift.redeem.link";
    public static final String EVENT_GIFT_REDEEM_SCAN = "gift.redeem.scan";
    public static final String EVENT_GIFT_REDEEM_RECLAIM = "gift.redeem.reclaim";
    // Staking
    public static final String EVENT_WALLET_STAKE = "wallet.xtz.stake";
    public static final String EVENT_WALLET_UNSTAKE = "wallet.xtz.unstake";
    private static final String TAG = EventUtils.class.getName();
    private static final String EVENTS_PATH = "/events";
    private static final String SESSION_ID = "sessionId";
    private static final String TIME = "time";
    private static final String EVENT_NAME = "eventName";
    private static final String METADATA = "metadata";
    private static final String APP_VERSION = "appVersion";
    private static final String DEVICE_TYPE = "deviceType";
    private static final String EVENTS = "events";
    private static final String ERROR_MESSAGE_ATTRIBUTE_KEY = "errorMessage";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    // Resolvable Address
    public static final String EVENT_NAME_RESOLVED = "nameresolved";
    public static final String EVENT_ATTRIBUTE_SERVICE = "service";
    public static final String EVENT_SERVICE_CNS = "cns";
    public static final String EVENT_SERVICE_ENS = "ens";
    public static final String EVENT_SERVICE_FIO = "fio";
    public static final String EVENT_SERVICE_PAY = "paystring";

    private static final String EVENTS_FOLDER_NAME = "events";

    private static String mSessionId;
    private static List<Event> mEvents = new ArrayList<>();

    private EventUtils() {
        //SessionId needs to be renewed for every app session.
        mSessionId = UUID.randomUUID().toString();
    }

    public static final String getEventNameWithCurrencyCode(String eventName, String currencyCode) {
        return String.format(eventName, currencyCode.toUpperCase(Locale.ROOT));
    }

    public static synchronized void pushEvent(String eventName, Map<String, String> attributes) {
        Log.d(TAG, "pushEvent: " + eventName + ", attr: " + attributes);
        Event event = new Event(System.currentTimeMillis() / DateUtils.SECOND_IN_MILLIS, eventName, attributes);
        mEvents.add(event);
    }

    public static synchronized void pushEvent(String eventName) {
        pushEvent(eventName, null);
    }

    public static synchronized void saveEvents(Context context) {
        JSONArray eventsJsonArray = new JSONArray();
        if (context != null) {
            for (Event event : mEvents) {
                JSONObject eventJsonObject = new JSONObject();
                try {
                    eventJsonObject.put(SESSION_ID, mSessionId);
                    eventJsonObject.put(TIME, event.getTime());
                    eventJsonObject.put(EVENT_NAME, event.getEventName());
                    if (event.getAttributes() != null && event.getAttributes().size() > 0) {
                        JSONArray metadataJsonArray = new JSONArray();
                        for (Map.Entry<String, String> entry : event.getAttributes().entrySet()) {
                            JSONObject metadataJsonObject = new JSONObject();
                            metadataJsonObject.put(KEY, entry.getKey());
                            metadataJsonObject.put(VALUE, entry.getValue());
                            metadataJsonArray.put(metadataJsonObject);
                        }
                        eventJsonObject.put(METADATA, metadataJsonArray);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "saveEvents: ", e);
                }
                eventsJsonArray.put(eventJsonObject);
            }
            writeEventsToDisk(context, eventsJsonArray.toString());
        } else {
            Log.e(TAG, "saveEvents: FAILED TO WRITE EVENTS TO FILE: app is null");
        }
    }

    public static void pushToServer(Context context) {
        Log.d(TAG, "pushToServer()");
        if (context != null) {
            Map<String, JSONArray> eventsMap = getEventsFromDisk(context);
            JSONArray aggregatedEventsArray = new JSONArray();
            if (eventsMap != null) {
                Log.e(TAG, "pushToServer: files: " + eventsMap.size());
                for (Map.Entry<String, JSONArray> entry : eventsMap.entrySet()) {
                    JSONArray eventChain = entry.getValue();
                    for (int i = 0; i < eventChain.length(); i++) {
                        try {
                            aggregatedEventsArray.put(eventChain.getJSONObject(i));
                        } catch (JSONException e) {
                            Log.e(TAG, "pushToServer: ", e);
                        }
                    }
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(DEVICE_TYPE, 1);
                    jsonObject.put(APP_VERSION, String.valueOf(BuildConfig.VERSION_CODE));
                    jsonObject.put(EVENTS, aggregatedEventsArray);
                } catch (JSONException e) {
                    Log.e(TAG, "pushToServer: ", e);
                    return;
                }

                RequestBody requestBody = RequestBody.create(MediaType.parse(BRConstants.CONTENT_TYPE_JSON_CHARSET_UTF8), jsonObject.toString());
                Request request = new Request.Builder()
                        .url(APIClient.getBaseURL() + EVENTS_PATH)
                        .header(BRConstants.HEADER_CONTENT_TYPE, BRConstants.CONTENT_TYPE_JSON_CHARSET_UTF8)
                        .header(BRConstants.HEADER_ACCEPT, BRConstants.CONTENT_TYPE_JSON_CHARSET_UTF8)
                        .post(requestBody).build();

                APIClient.BRResponse response = APIClient.getInstance(context).sendRequest(request, true);
                Log.d(TAG, "pushToServer: response: " + response.getCode() + ", " + response.getBodyText());

                if (response.isSuccessful()) {
                    //if sent then remove the local files.
                    File directory = new File(context.getFilesDir() + File.separator + EVENTS_FOLDER_NAME);
                    if (!directory.exists()) {
                        if (!directory.mkdir()) {
                            Log.e(TAG, "pushToServer: Failed to create directory: " + directory.getAbsolutePath());
                        }
                    }
                    for (Map.Entry<String, JSONArray> entry : eventsMap.entrySet()) {
                        if (!new File(directory, entry.getKey()).delete()) {
                            Log.e(TAG, "pushToServer: Failed to delete file: " + entry.getKey());
                        }
                    }
                } else {
                    Log.e(TAG, "pushToServer: response unsuccessful: "
                            + response.getCode() + " -> " + response.getBodyText());
                }
            }
        } else {
            Log.e(TAG, "pushToServer: Failed to push, app is null");
        }
    }

    private static void writeEventsToDisk(Context context, String jsonData) {
        File directory = getEventsDirectory(context);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                Log.e(TAG, "writeEventsToDisk: Failed to create directory: " + directory.getAbsolutePath());
            }
        }
        File newFile = new File(directory, UUID.randomUUID().toString());
        if (!newFile.exists()) {
            try {
                if (!newFile.createNewFile()) {
                    Log.e(TAG, "writeEventsToDisk: Failed to create file: " + newFile.getAbsolutePath());
                }
            } catch (IOException e) {
                Log.e(TAG, "writeEventsToDisk: ", e);
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
            fileOutputStream.write(jsonData.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "writeEventsToDisk: ", e);
        }
    }

    private static Map<String, JSONArray> getEventsFromDisk(Context context) {
        Log.d(TAG, "getEventsFromDisk()");
        Map<String, JSONArray> resultMap = new HashMap<>();
        File directory = getEventsDirectory(context);
        File[] fileList = directory.listFiles();
        if (fileList == null || fileList.length == 0) {
            return null;
        }
        for (File file : fileList) {
            if (file.isFile()) {
                String fileName = file.getName();
                String fileData = readFile(directory, file.getName());
                if (!Utils.isNullOrEmpty(fileData)) {
                    try {
                        JSONArray jsonArray = new JSONArray(fileData);
                        resultMap.put(fileName, jsonArray);
                    } catch (JSONException e) {
                        Log.e(TAG, "getEventsFromDisk: ", e);
                    }
                }
            } else {
                Log.e(TAG, "getEventsFromDisk: Unexpected directory where file is expected: " + file.getName());
            }
        }

        Log.d(TAG, "getEventsFromDisk result - > " + resultMap.size());
        return resultMap;
    }

    private static File getEventsDirectory(Context context) {
        return new File(context.getFilesDir() + File.separator + EVENTS_FOLDER_NAME);
    }

    private static String readFile(File directory, String fileName) {
        File file = new File(directory, fileName);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return IOUtils.toString(inputStream);
        } catch (IOException e) {
            Log.e(TAG, "readFile: Error in Reading: " + directory + "/" + fileName, e);
            BRReportsManager.reportBug(e);
            return null;
        }
    }

    /**
     * Send an event of transaction SUCCESS if error is null or FAIL if error is present.
     *
     * @param error - the error in case of transaction failed, null if transaction succeeded
     */
    public static void sendTransactionEvent(String error) {
        if (Utils.isNullOrEmpty(error)) {
            EventUtils.pushEvent(EventUtils.EVENT_SEND_SUCCESS);
        } else {
            Map<String, String> attributes = new HashMap<>();
            attributes.put(ERROR_MESSAGE_ATTRIBUTE_KEY, error);
            EventUtils.pushEvent(EventUtils.EVENT_SEND_PUBLISH_FAILED, attributes);
        }
    }

    public static class Event {
        private long mTime;
        private String mEventName;
        private Map<String, String> mAttributes;

        public Event(long time, String eventName, Map<String, String> attributes) {
            mTime = time;
            mEventName = eventName;
            mAttributes = attributes;
        }

        public long getTime() {
            return mTime;
        }

        String getEventName() {
            return mEventName;
        }

        Map<String, String> getAttributes() {
            return mAttributes;
        }
    }
}
