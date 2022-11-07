package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.CATEGORY_DESC_FRONTEND;
import static seedu.address.logic.commands.CommandTestUtil.DEADLINE_DESC_A;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_B;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_CATEGORY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DEADLINE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRIORITY_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PRIORITY_DESC_LOW;
import static seedu.address.logic.commands.CommandTestUtil.TASK_NAME_DESC_A;
import static seedu.address.logic.commands.CommandTestUtil.TEST_CATEGORY_FRONTEND;
import static seedu.address.logic.commands.CommandTestUtil.TEST_CATEGORY_OTHERS;
import static seedu.address.logic.commands.CommandTestUtil.TEST_DEADLINE_TOMORROW;
import static seedu.address.logic.commands.CommandTestUtil.TEST_DESCRIPTION_ONE;
import static seedu.address.logic.commands.CommandTestUtil.TEST_PRIORITY_LOW;
import static seedu.address.logic.commands.CommandTestUtil.TEST_PRIORITY_MEDIUM;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASK_NAME_A;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TASK_NAME_B;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TASK;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_TASK;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditTaskCommand;
import seedu.address.logic.commands.EditTaskCommand.EditTaskDescriptor;
import seedu.address.model.task.Priority;
import seedu.address.model.task.TaskCategory;
import seedu.address.model.task.TaskDeadline;
import seedu.address.testutil.EditTaskDescriptorBuilder;

public class EditTaskCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTaskCommand.MESSAGE_USAGE);

    private EditTaskCommandParser parser = new EditTaskCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_TASK_NAME_A, MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        // no parameter specified
        assertParseFailure(parser, "1", EditTaskCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + TASK_NAME_DESC_A, MESSAGE_INVALID_TASK_DISPLAYED_INDEX);

        // zero index
        assertParseFailure(parser, "0" + TASK_NAME_DESC_A, MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid task priority
        assertParseFailure(parser, "1" + INVALID_PRIORITY_DESC, Priority.MESSAGE_CONSTRAINTS);

        // invalid task category
        assertParseFailure(parser, "1" + INVALID_CATEGORY_DESC, TaskCategory.MESSAGE_CONSTRAINTS);

        // invalid task deadline
        assertParseFailure(parser, "1" + INVALID_DEADLINE_DESC, TaskDeadline.MESSAGE_CONSTRAINTS);

        // invalid task category followed by valid task deadline
        assertParseFailure(
                parser, "1" + INVALID_CATEGORY_DESC + DEADLINE_DESC_A, TaskCategory.MESSAGE_CONSTRAINTS);

        // valid task category followed by invalid task category. The test case for invalid task category followed by
        // valid task category is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(
                parser, "1" + CATEGORY_DESC_FRONTEND + INVALID_CATEGORY_DESC, TaskCategory.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_PRIORITY_DESC + INVALID_CATEGORY_DESC + INVALID_DEADLINE_DESC,
                Priority.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_TASK;
        String userInput = targetIndex.getOneBased() + TASK_NAME_DESC_A + DESCRIPTION_DESC_B
                + PRIORITY_DESC_LOW + CATEGORY_DESC_FRONTEND + DEADLINE_DESC_A;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_TASK_NAME_B)
                .withDescription(TEST_DESCRIPTION_ONE).withCategory(TEST_CATEGORY_OTHERS)
                .withPriority(TEST_PRIORITY_MEDIUM).withDeadline(TEST_DEADLINE_TOMORROW).build();
        EditTaskCommand expectedCommand = new EditTaskCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_TASK;
        String userInput = targetIndex.getOneBased() + CATEGORY_DESC_FRONTEND + DEADLINE_DESC_A;

        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder()
                .withCategory(TEST_CATEGORY_OTHERS).withDeadline(TEST_DEADLINE_TOMORROW).build();
        EditTaskCommand expectedCommand = new EditTaskCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // task name
        Index targetIndex = INDEX_THIRD_TASK;
        String userInput = targetIndex.getOneBased() + TASK_NAME_DESC_A;
        EditTaskDescriptor descriptor = new EditTaskDescriptorBuilder().withName(VALID_TASK_NAME_A).build();
        EditTaskCommand expectedCommand = new EditTaskCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // task priority
        userInput = targetIndex.getOneBased() + PRIORITY_DESC_LOW;
        descriptor = new EditTaskDescriptorBuilder().withPriority(TEST_PRIORITY_LOW).build();
        expectedCommand = new EditTaskCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // task category
        userInput = targetIndex.getOneBased() + CATEGORY_DESC_FRONTEND;
        descriptor = new EditTaskDescriptorBuilder().withCategory(TEST_CATEGORY_FRONTEND).build();
        expectedCommand = new EditTaskCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
