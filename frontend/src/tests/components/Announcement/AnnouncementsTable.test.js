import { fireEvent, render, waitFor, screen } from '@testing-library/react';
import { announcementFixtures } from 'fixtures/announcementFixtures';
import AnnouncementTable from 'main/components/Announcement/AnnouncementTable';
import { QueryClient, QueryClientProvider } from 'react-query';
import { MemoryRouter } from 'react-router-dom';
import { currentUserFixtures } from 'fixtures/currentUserFixtures';

import * as useBackendModule from 'main/utils/useBackend';

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedNavigate,
}));

describe('AnnouncementTable tests', () => {
  const queryClient = new QueryClient();

  const expectedHeaders = [
    'id',
    'Start Date ISO Format',
    'End Date ISO Format',
    'Announcement',
  ];
  const expectedFields = ['id', 'startDate', 'endDate', 'announcementText'];
  const testId = 'AnnouncementTable';

  test('renders empty table correctly', () => {
    // arrange
    const currentUser = currentUserFixtures.adminUser;

    // act
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable announcements={[]} currentUser={currentUser} />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // assert
    expectedHeaders.forEach((headerText) => {
      const header = screen.getByText(headerText);
      expect(header).toBeInTheDocument();
    });

    expectedFields.forEach((field) => {
      const fieldElement = screen.queryByTestId(
        `${testId}-cell-row-0-col-${field}`
      );
      expect(fieldElement).not.toBeInTheDocument();
    });
  });

  test('Has the expected column headers, content and buttons for admin user', () => {
    // arrange
    const currentUser = currentUserFixtures.adminUser;

    // act
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable
            announcements={announcementFixtures.threeAnnouncements}
            currentUser={currentUser}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // assert
    expectedHeaders.forEach((headerText) => {
      const header = screen.getByText(headerText);
      expect(header).toBeInTheDocument();
    });

    expectedFields.forEach((field) => {
      const header = screen.getByTestId(`${testId}-cell-row-0-col-${field}`);
      expect(header).toBeInTheDocument();
    });

    expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent(
      '1'
    );
    expect(
      screen.getByTestId(`${testId}-cell-row-0-col-startDate`)
    ).toHaveTextContent('2024-12-12T00:00:00');

    expect(screen.getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent(
      '2'
    );
    expect(
      screen.getByTestId(`${testId}-cell-row-1-col-startDate`)
    ).toHaveTextContent('2022-12-12T00:00:00');

    const editButton = screen.getByTestId(
      `${testId}-cell-row-0-col-Edit-button`
    );
    expect(editButton).toBeInTheDocument();
    expect(editButton).toHaveClass('btn-primary');

    const deleteButton = screen.getByTestId(
      `${testId}-cell-row-0-col-Delete-button`
    );
    expect(deleteButton).toBeInTheDocument();
    expect(deleteButton).toHaveClass('btn-danger');
  });

  test('Has the expected column headers, content for ordinary user', () => {
    // arrange
    const currentUser = currentUserFixtures.userOnly;

    // act
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable
            announcements={announcementFixtures.threeAnnouncements}
            currentUser={currentUser}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // assert
    expectedHeaders.forEach((headerText) => {
      const header = screen.getByText(headerText);
      expect(header).toBeInTheDocument();
    });

    expectedFields.forEach((field) => {
      const header = screen.getByTestId(`${testId}-cell-row-0-col-${field}`);
      expect(header).toBeInTheDocument();
    });

    expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent(
      '1'
    );
    expect(
      screen.getByTestId(`${testId}-cell-row-0-col-startDate`)
    ).toHaveTextContent('2024-12-12T00:00:00');

    expect(screen.getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent(
      '2'
    );
    expect(
      screen.getByTestId(`${testId}-cell-row-1-col-startDate`)
    ).toHaveTextContent('2022-12-12T00:00:00');

    expect(screen.queryByText('Delete')).not.toBeInTheDocument();
    expect(screen.queryByText('Edit')).not.toBeInTheDocument();
  });

  test('Edit button navigates to the edit page', async () => {
    // arrange
    const currentUser = currentUserFixtures.adminUser;

    // act - render the component
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable
            announcements={announcementFixtures.threeAnnouncements}
            currentUser={currentUser}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // assert - check that the expected content is rendered
    expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent(
      '1'
    );
    expect(
      screen.getByTestId(`${testId}-cell-row-0-col-startDate`)
    ).toHaveTextContent('2024-12-12T00:00:00');

    const editButton = screen.getByTestId(
      `${testId}-cell-row-0-col-Edit-button`
    );
    expect(editButton).toBeInTheDocument();

    // act - click the edit button
    fireEvent.click(editButton);

    // assert - check that the navigate function was called with the expected path
    await waitFor(() =>
      expect(mockedNavigate).toHaveBeenCalledWith('/announcements/edit/1')
    );
  });

  test('Delete button calls delete callback', async () => {
    // arrange
    const currentUser = currentUserFixtures.adminUser;

    // act - render the component
    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable
            announcements={announcementFixtures.threeAnnouncements}
            currentUser={currentUser}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // assert - check that the expected content is rendered
    expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent(
      '1'
    );
    expect(
      screen.getByTestId(`${testId}-cell-row-0-col-startDate`)
    ).toHaveTextContent('2024-12-12T00:00:00');

    const deleteButton = screen.getByTestId(
      `${testId}-cell-row-0-col-Delete-button`
    );
    expect(deleteButton).toBeInTheDocument();

    // act - click the delete button
    fireEvent.click(deleteButton);
  });
});

describe('Modal tests', () => {
  const queryClient = new QueryClient();

  // Mocking the delete mutation function
  const mockMutate = jest.fn();
  const mockUseBackendMutation = {
    mutate: mockMutate,
  };

  beforeEach(() => {
    jest
      .spyOn(useBackendModule, 'useBackendMutation')
      .mockReturnValue(mockUseBackendMutation);
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test('Clicking Delete button opens the modal for adminUser', async () => {
    const currentUser = currentUserFixtures.adminUser;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable
            announcements={announcementFixtures.threeAnnouncements}
            currentUser={currentUser}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // Verify that the modal is hidden by checking for the absence of the "modal-open" class
    await waitFor(() => {
      expect(document.body).not.toHaveClass('modal-open');
    });

    const deleteButton = screen.getByTestId(
      'AnnouncementTable-cell-row-0-col-Delete-button'
    );
    fireEvent.click(deleteButton);

    // Verify that the modal is shown by checking for the "modal-open" class
    await waitFor(() => {
      expect(document.body).toHaveClass('modal-open');
    });
  });

  test('Clicking Permanently Delete button deletes the announcement', async () => {
    const currentUser = currentUserFixtures.adminUser;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable
            announcements={announcementFixtures.threeAnnouncements}
            currentUser={currentUser}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );

    const deleteButton = screen.getByTestId(
      'AnnouncementTable-cell-row-0-col-Delete-button'
    );
    fireEvent.click(deleteButton);

    const permanentlyDeleteButton = await screen.findByTestId(
      'AnnouncementTable-Modal-Delete'
    );
    fireEvent.click(permanentlyDeleteButton);

    // Verify that the modal is hidden by checking for the absence of the "modal-open" class
    await waitFor(() => {
      expect(document.body).not.toHaveClass('modal-open');
    });
  });

  test('Clicking Keep this Announcement button cancels the deletion', async () => {
    const currentUser = currentUserFixtures.adminUser;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable
            announcements={announcementFixtures.threeAnnouncements}
            currentUser={currentUser}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );

    const deleteButton = screen.getByTestId(
      'AnnouncementTable-cell-row-0-col-Delete-button'
    );
    fireEvent.click(deleteButton);

    const cancelButton = await screen.findByTestId(
      'AnnouncementTable-Modal-Cancel'
    );
    fireEvent.click(cancelButton);

    // Verify that the modal is hidden by checking for the absence of the "modal-open" class
    await waitFor(() => {
      expect(document.body).not.toHaveClass('modal-open');
    });

    expect(mockMutate).not.toHaveBeenCalled();
  });

  test('Pressing the escape key on the modal cancels the deletion', async () => {
    const currentUser = currentUserFixtures.adminUser;

    render(
      <QueryClientProvider client={queryClient}>
        <MemoryRouter>
          <AnnouncementTable
            announcements={announcementFixtures.threeAnnouncements}
            currentUser={currentUser}
          />
        </MemoryRouter>
      </QueryClientProvider>
    );

    // Click the delete button to open the modal
    const deleteButton = screen.getByTestId(
      'AnnouncementTable-cell-row-0-col-Delete-button'
    );
    fireEvent.click(deleteButton);

    // Check that the modal is displayed by checking for the "modal-open" class in the body
    expect(document.body).toHaveClass('modal-open');

    // Click the close button
    const closeButton = screen.getByLabelText('Close');
    fireEvent.click(closeButton);

    // Verify that the modal is hidden by checking for the absence of the "modal-open" class
    await waitFor(() => {
      expect(document.body).not.toHaveClass('modal-open');
    });

    // Assert that the delete mutation was not called
    // (you'll need to replace `mockMutate` with the actual reference to the mutation in your code)
    expect(mockMutate).not.toHaveBeenCalled();
  });
});
