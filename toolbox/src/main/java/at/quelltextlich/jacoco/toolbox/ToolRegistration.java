package at.quelltextlich.jacoco.toolbox;

public class ToolRegistration implements Comparable<ToolRegistration> {
  private final String commandName;
  private final String description;
  private final Class<?> toolClass;
  private final boolean visible;

  public <T extends Tool> ToolRegistration(final String commandName,
      final String description, final Class<T> toolClass) {
    this(commandName, description, toolClass, true);
  }

  public <T extends Tool> ToolRegistration(final String commandName,
      final String description, final Class<T> toolClass, final boolean visible) {
    super();
    this.commandName = commandName;
    this.description = description;
    this.toolClass = toolClass;
    this.visible = visible;
  }

  public String getCommandName() {
    return commandName;
  }

  public String getDescription() {
    return description;
  }

  public <T extends Tool> Class<T> getToolClass() {
    @SuppressWarnings("unchecked")
    final Class<T> castClass = (Class<T>) toolClass;
    return castClass;
  }

  public boolean isVisible() {
    return visible;
  }

  @Override
  public int compareTo(final ToolRegistration o) {
    int ret = commandName.compareTo(o.commandName);
    if (ret == 0) {
      ret = description.compareTo(o.description);
    }
    if (ret == 0) {
      ret = toolClass.hashCode() - o.toolClass.hashCode();
    }
    return ret;
  }
}
