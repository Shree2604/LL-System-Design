public class VideoConsumingService {

    private Database database = new Database();

    // Sets the seek time for a watched video
    public int seekTime(String videoID, String userID, int seekTime) {
        WatchedVideo watchedVideo = database.getWatchedVideo(videoID, userID);
        if (watchedVideo != null) {
            watchedVideo.seekTime = seekTime;
            return watchedVideo.seekTime;
        }
        return -1;
    }

    // Main method to test functionality
    public static void main(String[] args) {
        // --- Test 1: SeekTime ---
        VideoConsumingService service = new VideoConsumingService();
        int result = service.seekTime("video1", "user1", 50);
        System.out.println("Seek Time set to: " + result);

        // --- Test 2: GetFrame ---
        VideoService videoService = new VideoService();

        // Prepare a sample video with frames
        Video video = new Video();
        video.id = "video1";
        video.frames = new Frame[3];
        video.frames[0] = new Frame(0, 9);
        video.frames[1] = new Frame(10, 19);
        video.frames[2] = new Frame(20, 29);

        // Save video to filesystem
        videoService.fileSystem.setVideo(video);

        // Retrieve a frame at timestamp 12
        Frame frame = videoService.getFrame("video1", 12);
        if (frame != null) {
            System.out.println("Frame found: " + frame.startTimestamp + " to " + frame.endTimestamp);
        } else {
            System.out.println("Frame not found!");
        }
    }
}

// ---------------- Supporting Classes ----------------

class VideoService {
    FileSystem fileSystem = new FileSystem();

    public Frame getFrame(String videoID, int timestamp) {
        Video video = fileSystem.getVideo(videoID);
        if (video != null) {
            return video.getFrame(timestamp);
        }
        return null;
    }
}

class FileSystem {
    private Video storedVideo; // Simple in-memory store

    public void setVideo(Video video) {
        this.storedVideo = video;
    }

    public Video getVideo(String videoID) {
        if (storedVideo != null && storedVideo.id.equals(videoID)) {
            return storedVideo;
        }
        return null;
    }
}

class Database {
    public WatchedVideo getWatchedVideo(String videoID, String userID) {
        WatchedVideo wv = new WatchedVideo();
        wv.videoID = videoID;
        wv.userID = userID;
        return wv;
    }
}

class Video {
    String id;
    Frame[] frames;
    String jsonMetadata;

    public Frame getFrame(int timestamp) {
        if (frames == null) throw new IndexOutOfBoundsException("No frames available");
        for (Frame frame : frames) {
            if (frame.startTimestamp <= timestamp && frame.endTimestamp >= timestamp) {
                return frame;
            }
        }
        return null;
    }
}

class User {
    String id;
    String name;
    String email;

    public String getId() {
        return id;
    }
}

class Frame {
    public static int frametime = 10;
    byte[] bytes;
    int startTimestamp;
    int endTimestamp;

    public Frame(int start, int end) {
        this.startTimestamp = start;
        this.endTimestamp = end;
    }
}

class WatchedVideo {
    String id;
    String videoID;
    String userID;
    int seekTime;

    public int getSeekTime() {
        return seekTime;
    }
}
