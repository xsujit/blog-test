Feature: Do comments under a post have a valid email?
  A post can have multiple comments
  These comments should have a valid email

  Scenario: Comments under Delphine's post have valid emails
    Given I am user "Delphine"
    When I search for posts written by me
    Then I should see each comment has a valid email