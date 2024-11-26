import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import CoursesCreatePage from "main/pages/CoursesCreatePage";

import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";

import axios from "axios";
import AxiosMockAdapter from "axios-mock-adapter";

const mockToast = jest.fn();
jest.mock('react-toastify', () => {
    const originalModule = jest.requireActual('react-toastify');
    return {
        __esModule: true,
        ...originalModule,
        toast: (x) => mockToast(x)
    };
});

const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => {
    const originalModule = jest.requireActual('react-router-dom');
    return {
        __esModule: true,
        ...originalModule,
        Navigate: (x) => { mockNavigate(x); return null; }
    };
});

describe("CourseCreatePage tests", () => {

    const axiosMock =new AxiosMockAdapter(axios);

    beforeEach(() => {
        jest.clearAllMocks();
        axiosMock.reset();
        axiosMock.resetHistory();
        axiosMock.onGet("/api/currentUser").reply(200, apiCurrentUserFixtures.userOnly);
        axiosMock.onGet("/api/systemInfo").reply(200, systemInfoFixtures.showingNeither);
    });

    const queryClient = new QueryClient();
    test("renders without crashing", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CoursesCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("on submit, makes request to backend", async () => {

        const queryClient = new QueryClient();
        const course = {
            id: 1,
            name: "CS156",
            term: "F23",
        };

        axiosMock.onPost("/api/courses/post").reply(202, course);

        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <CoursesCreatePage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        await waitFor(() => {
            expect(screen.getByTestId("CoursesForm-name")).toBeInTheDocument();
        });

        const nameField = screen.getByTestId("CoursesForm-name");
        const termField = screen.getByTestId("CoursesForm-term");
        const submitButton = screen.getByTestId("CoursesForm-submit");

        fireEvent.change(nameField, { target: { value: 'CS156' } });
        fireEvent.change(termField, { target: { value: 'F23' } });

        expect(submitButton).toBeInTheDocument();

        fireEvent.click(submitButton);

        await waitFor(() => expect(axiosMock.history.post.length).toBe(1));

        expect(axiosMock.history.post[0].params).toEqual(
            {
                "name": "CS156",
                "term": "F23"
        });

        expect(mockToast).toBeCalledWith("New course created - id: 1");
        expect(mockNavigate).toBeCalledWith({ "to": "/courses" });
    });


});
