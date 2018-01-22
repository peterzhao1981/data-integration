package com.mode.google.content;

import com.google.api.services.content.ShoppingContent;
import com.google.api.services.content.ShoppingContentScopes;
import com.mode.google.common.Authenticator;
import com.mode.google.common.BaseSample;

import java.io.File;
import java.io.IOException;


/** Base class for the Content API samples. */
public abstract class ContentSample extends BaseSample {
  protected ContentConfig config;
  protected ShoppingContent content;
  protected ShoppingContent sandbox;

  public ContentSample(String args[]) throws IOException {
    super(args);
    ShoppingContent.Builder builder =
        new ShoppingContent.Builder(httpTransport, jsonFactory, initializer)
            .setApplicationName("Content API for Shopping Samples");
    content = createService(builder);
    sandbox = createSandboxContentService(builder);
    retrieveConfiguration();
  }

  protected void loadConfig(File path) throws IOException {
    config = ContentConfig.load(path);
  }

  // This method assumes a builder that's already been used to create the standard
  // ShoppingContent service object, so we'll just modify the path if needed.
  protected ShoppingContent createSandboxContentService(ShoppingContent.Builder builder) {
    return ContentWorkflowSample.createSandboxContentService(builder);
  }

  protected Authenticator loadAuthentication() throws IOException {
    return new Authenticator(httpTransport, jsonFactory, ShoppingContentScopes.all(), config);
  }

  protected void retrieveConfiguration() throws IOException {
    ContentWorkflowSample.retrieveConfiguration(content, config);
  }

  protected void checkMCA() {
    if (!config.getIsMCA()) {
      throw new IllegalStateException(
          "Sample requires the authenticating account to be a multi-client account");
    }
  }

  protected void checkNonMCA() {
    if (config.getIsMCA()) {
      throw new IllegalStateException(
          "Sample requires the authenticating account to be a non-multi-client account");
    }
  }
}
