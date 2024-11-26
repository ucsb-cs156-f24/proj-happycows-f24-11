import { fireEvent, render, waitFor, screen } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import CoursesIndexPage from "main/pages/CoursesIndexPage";


import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import { coursesFixtures } from "fixtures/coursesFixtures";
import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";
import mockConsole from "jest-mock-console";


const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

describe("CoursesIndexPage tests", () => {

    const axiosMock = new AxiosMockAdapter(axios);

    const testId = "CoursesTable";

    const setupAdminUser = () => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.adminUser);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    };

    const setupUser = () => {
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    }

    test("Renders with Create Button for admin user", async () => {
        // arrange
        setupAdminUser();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/courses/all").reply(200, []);

        // act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CoursesIndexPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        // assert
        await waitFor( ()=>{
            expect(screen.getByText(/Create Course/)).toBeInTheDocument();
        });
        const button = screen.getByText(/Create Course/);
        expect(button).toHaveAttribute("href", "/courses/create");
        expect(button).toHaveAttribute("style", "float: right;");
    });
    
    test("Renders without Create Button for non admin user", async () => {
        // arrange
        setupUser(); 
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/courses/all").reply(200, []);
    
        // act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CoursesIndexPage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    
        // assert
        await waitFor(() => {
            expect(screen.queryByText(/Create Course/)).not.toBeInTheDocument();
        });
    });
    
    test("renders three courses correctly for admin", async () => {    
        // arrange
        setupAdminUser();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/courses/all").reply(200, coursesFixtures.threeCourses);

        // act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CoursesIndexPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        // assert
        await waitFor(() => { expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("1"); });
        expect(screen.getByTestId(`${testId}-cell-row-1-col-id`)).toHaveTextContent("2");
        expect(screen.getByTestId(`${testId}-cell-row-2-col-id`)).toHaveTextContent("3");

    });

    test("renders empty table when backend unavailable, admin", async () => {
        // arrange
        setupAdminUser();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/courses/all").timeout();
        const restoreConsole = mockConsole();

        // act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CoursesIndexPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        // assert
        await waitFor(() => { expect(axiosMock.history.get.length).toBeGreaterThanOrEqual(1); });

        restoreConsole();

        expect(screen.queryByTestId(`${testId}-cell-row-0-col-id`)).not.toBeInTheDocument();
    });

    test("what happens when you click delete, admin", async () => {
        // arrange
        setupAdminUser();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/courses/all").reply(200, coursesFixtures.threeCourses);
        axiosMock.onDelete("/api/courses/delete").reply(200, "Course with id 1 was deleted");

        // act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CoursesIndexPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        // assert
        await waitFor(() => { expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toBeInTheDocument(); });

        expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("1");

        const deleteButton = screen.getByTestId(`${testId}-cell-row-0-col-Delete-button`);
        expect(deleteButton).toBeInTheDocument();

        // act
        fireEvent.click(deleteButton);

        // assert
        await waitFor(() => { expect(mockToast).toBeCalledWith("Course with id 1 was deleted") });

    });

    test("tests buttons for editing do not show up for user", async () => {
        // arrange
        setupUser();
        const queryClient = new QueryClient();
        axiosMock.onGet("/api/courses/all").reply(200, coursesFixtures.threeCourses);

        // act
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CoursesIndexPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        // assert
        await waitFor(() => { expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toBeInTheDocument(); });

        expect(screen.getByTestId(`${testId}-cell-row-0-col-id`)).toHaveTextContent("1");

        const deleteButton = screen.queryByTestId(`${testId}-cell-row-0-col-Delete-button`);
        expect(deleteButton).not.toBeInTheDocument();
        expect(screen.queryByText(/Create Course/)).not.toBeInTheDocument();


        // assert

    });

});

