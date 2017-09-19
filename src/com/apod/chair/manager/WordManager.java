package com.apod.chair.manager;

import java.io.File;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.apod.chair.bo.Word;
import com.apod.chair.http.Web;
import com.apod.chair.util.Storage;
import com.apod.chair.util.Utils;
import com.apod.chair.util.WordUtils;

public class WordManager {
	private final static String TAG = Config.TAG + ".WordManager";
	private static WordManager mWordManager;
	private OnWordListener listener;
	private Context mContext;

	private WordManager(Context context) {
		mContext = context;
	}

	public static WordManager getInstance(Context context) {
		if (mWordManager == null) {
			mWordManager = new WordManager(context);
		}
		return mWordManager;
	}

	private Word readWord(Word word) {
		String filepath = word.getFilepath();
		String dirname = Storage.convertNameByPath(filepath);
		// 判断文件名获取是否正确
		if (dirname == null) {
			Log.d(TAG, "[" + filepath + "] dirname is null,so return.");
			word.setState(Word.READ_FILE_NOT_FAIL);
			return word;
		}
		String dirpath = word.getDir() + dirname;
		Log.d(TAG, "reword file path is " + filepath);
		Log.d(TAG, "reword dir path is " + dirpath);
		File file = new File(filepath);
		File dirfile = new File(dirpath);
		if (Utils.isWifiConnected(mContext)) {
			Log.d(TAG, "wifi is connected , try to dowload it.");
			if (file.exists()) {
				Log.d(TAG, "delete file " + filepath);
				file.delete();
			}
			if (dirfile.exists()) {
				Log.d(TAG, "delete file " + dirfile);
				file.delete();
			}
			// 开始下载docx文档
			boolean state = Web.executeDowloadDocx(word);
			if (listener != null) {
				listener.onDowloadWord(word, state);
			}
			Log.d(TAG, "dowload docx file state is " + state);
			if (!state) {
				word.setState(Word.READ_FILE_NOT_FOUND);
				return word;
			}
		}
		// 判断word文件是否存在
		if (!file.exists()) {
			Log.d(TAG, "[" + filepath + "] is not exists,so return.");
			word.setState(Word.READ_FILE_NOT_FOUND);
			return word;
		}

		// 创建Word文件名命名的文件夹
		if (!dirfile.exists()) {
			boolean state = dirfile.mkdir();
			if (!state) {
				word.setState(Word.READ_FILE_NOT_FAIL);
				Log.d(TAG, "[" + dirpath + "] mkdir failer,so return.");
				return word;
			}
		}
		word.setFilename(dirname);
		WordUtils wordUtils = new WordUtils(word);
		word = wordUtils.readSync();
		return word;
	}

	public void executeWord(Word word) {
		new WebAsyncTask().execute(word);
	}

	public class WebAsyncTask extends AsyncTask<Word, Void, Word> {
		protected Word doInBackground(Word... params) {
			if (listener != null) {
				listener.onLoadingWord(params[0]);
			}
			Word word = readWord(params[0]);
			return word;
		}

		protected void onPostExecute(Word word) {
			if (listener != null) {
				listener.onLoadedWord(word);
			}
		}

	}

	public interface OnWordListener {
		public void onLoadingWord(Word word);

		public void onDowloadWord(Word word, boolean state);

		public void onLoadedWord(Word word);
	}

	public void setOnWordListener(OnWordListener listener) {
		this.listener = listener;
	}
}
