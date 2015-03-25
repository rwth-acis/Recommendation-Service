/**
 * 
 */
package i5.las2peer.services.servicePackage.datamodel;

import i5.las2peer.services.servicePackage.semanticTagger.SemanticTagger;
import i5.las2peer.services.servicePackage.textProcessor.StopWordRemover;
import i5.las2peer.services.servicePackage.xmlparsers.Resource;
import i5.las2peer.services.servicePackage.xmlparsers.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

/**
 * @author sathvik
 *
 */
public class DatabaseHandler extends MySqlOpenHelper {

	/**
	 * @param dbName
	 * @param username
	 * @param password
	 */
	public DatabaseHandler(String dbName, String username, String password) {
		super(dbName, username, password);
	}

	public void addPosts(ArrayList<Resource> posts) throws SQLException {

		ConnectionSource source = super.getConnectionSource();
		Dao<DataEntity, Long> DataDao = DaoManager.createDao(source,
				DataEntity.class);

		DataEntity data = null;
		StopWordRemover remover = null;

		for (Resource res : posts) {
			data = new DataEntity();
			// boolean idExists = false;

			if (res.getPostId() != null) {
				Long postid = Long.parseLong(res.getPostId());
				data.setPostId(postid);
				// idExists = DataDao.idExists(postid);
				// System.out.println(postid + " " + idExists);
			}

			if (res.getAccAnsId() != null) {
				data.setAcceptAnsId(Long.parseLong(res.getAccAnsId()));
			}

			if (res.getAnswerCount() != null) {
				data.setAnswerCount(Long.parseLong(res.getAnswerCount()));
			}

			if (res.getCommentCount() != null) {
				data.setCommentCount(Integer.parseInt(res.getCommentCount()));
			}

			if (res.getCreationDate() != null) {
				data.setCreationDate(res.getCreationDate());
			}

			if (res.getLastEditorUserId() != null) {
				data.setLastEditorUserId(Long.parseLong(res
						.getLastEditorUserId()));
			}

			if (res.getOwnerUserId() != null) {
				data.setOwnerUserId(Long.parseLong(res.getOwnerUserId()));
			}

			if (res.getParentId() != null) {
				data.setParentId(Long.parseLong(res.getParentId()));
			}

			if (res.getPostId() != null) {
				data.setPostId(Long.parseLong(res.getPostId()));
			}

			if (res.getPostTypeId() != null) {
				data.setPostTypeId(Integer.parseInt(res.getPostTypeId()));
			}

			if (res.getScore() != null) {
				data.setScore(Long.parseLong(res.getScore()));
			}

			if (res.getTags() != null) {
				data.setTags(res.getTags());
			}

			if (res.getTitle() != null) {
				data.setTitle(res.getTitle());
			}

			if (res.getViewCount() != null) {
				data.setViewCount(Long.parseLong(res.getViewCount()));
			}

			if (res.getBody() != null) {
				data.setBody(res.getBody());

				remover = new StopWordRemover(res.getBody());
				data.setCleanText(remover.getPlainText());
			}
			DataDao.createIfNotExists(data);
		}
	}

	public void addUsers(ArrayList<User> users) throws SQLException {
		ConnectionSource source = super.getConnectionSource();
		UserEntity entity = null;
		Dao<UserEntity, Long> UserDao = DaoManager.createDao(source,
				UserEntity.class);

		// Iterate tags and create DAO objects.
		for (User user : users) {
			entity = new UserEntity();

			if (user.getDownVotes() != null) {
				entity.setDownVotes(Long.parseLong(user.getDownVotes()));
			}

			if (user.getReputation() != null) {
				entity.setReputation(Long.parseLong(user.getReputation()));
			}

			if (user.getUpVotes() != null) {
				entity.setUpVotes(Long.parseLong(user.getUpVotes()));
			}

			if (user.getUserAccId() != null) {
				entity.setUserAccId(Long.parseLong(user.getUserAccId()));
			}

			if (user.getUserAccId() != null) {
				entity.setUserId(Long.parseLong(user.getUserId()));
			}

			if (user.getViews() != null) {
				entity.setViews(Long.parseLong(user.getViews()));
			}

			entity.setCreationDate(user.getCreationDate());
			entity.setAbtMe(user.getAbtMe());
			entity.setLocation(user.getLocation());
			entity.setUserName(user.getUserName());
			entity.setWebsiteUrl(user.getWebsiteUrl());

			UserDao.createIfNotExists(entity);
		}
	}

	public void addSemanticTags() throws SQLException {
		ConnectionSource source = super.getConnectionSource();
		Dao<SemanticTagEntity, Long> SemanticDao = DaoManager.createDao(source,
				SemanticTagEntity.class);

		// Iterate all the posts and extract tags from them.
		Dao<DataEntity, Long> postsDao = DaoManager.createDao(source,
				DataEntity.class);
		List<DataEntity> data_entites = postsDao.queryForAll();

		SemanticTagEntity tagEntity = null;
		SemanticTagger tagger = null;
		for (DataEntity entity : data_entites) {
			tagger = new SemanticTagger(entity.getBody());
			if (tagger != null && tagger.getTags() != null) {
				String tags = tagger.getTags().getTags();
				String annotations = tagger.getTags().getAnnotation();

				tagEntity = new SemanticTagEntity();
				tagEntity.setPostId(entity.getPostId());
				tagEntity.setAnnotations(annotations);
				tagEntity.setTags(tags);

				SemanticDao.createIfNotExists(tagEntity);
			}

		}

	}

	public List<UserEntity> getUserDAOs() throws SQLException {
		Dao<UserEntity, Long> userDao = DaoManager.createDao(
				super.getConnectionSource(), UserEntity.class);
		return userDao.queryForAll();
	}

	// public void createUserMap() throws SQLException {
	// Dao<UserEntity, Long> userDao = DaoManager.createDao(
	// super.getConnectionSource(),
	// UserEntity.class);
	// List<UserEntity> user_entites = userDao.queryForAll();
	// for (UserEntity entity : user_entites) {
	// Application.userId2userObj.put(entity.getUserId(), entity);
	// }
	// }

	// public void createDataMap() throws SQLException {
	// Dao<DataEntity, Long> dataDao = DaoManager.createDao(
	// super.getConnectionSource(), DataEntity.class);
	// List<DataEntity> data_entites = dataDao.queryForAll();
	// for (DataEntity entity : data_entites) {
	// Application.postId2postObj.put(entity.getPostId(), entity);
	// }
	// }
}