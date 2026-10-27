package colony;


public final class SurfaceLocation implements Location {

    public static final SurfaceLocation MARS_SURFACE = new SurfaceLocation();

    
    public String getName() {
        return "Martian Surface";
    }

    public boolean isOperational() {
        return true;
    }

    public boolean isSheltered() {
        return false;
    }

    public String toString() {
        return getName();
    }
}
