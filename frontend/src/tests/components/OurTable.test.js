import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import OurTable, { ButtonColumn, DateColumn, PlaintextColumn } from "main/components/OurTable";

describe("OurTable tests", () => {
    const generateRows = (num) =>
        Array.from({ length: num }, (_, i) => ({
            col1: `Hello ${i}`,
            col2: `World ${i}`,
            createdAt: `2021-04-01T04:00:00.000`,
            log: `foo\nbar\n  baz ${i}`,
        }));
    const threeRows = generateRows(3);
    const tenRows = generateRows(10);
    const elevenRows = generateRows(11);
    const thirtyRows = generateRows(30);
    const hundredRows = generateRows(100);

    const clickMeCallback = jest.fn();

    const columns = [
        {
            Header: 'Column 1',
            accessor: 'col1', // accessor is the "key" in the data
        },
        {
            Header: 'Column 2',
            accessor: 'col2',
        },
        ButtonColumn("Click", "primary", clickMeCallback, "testId"),
        DateColumn("Date", (cell) => cell.row.original.createdAt),
        PlaintextColumn("Log", (cell) => cell.row.original.log),
    ];
    test("test line 84", async () => {
        render(<OurTable columns={columns} data={threeRows} />);

        // Check that the button is rendered and clickable
        const button = await screen.findByTestId("testId-cell-row-0-col-Click-button");
        fireEvent.click(button);
        await waitFor(() => expect(clickMeCallback).toBeCalledTimes(1));

        // Test sorting on column headers
        const col1Header = screen.getByTestId("testid-header-col1");
        fireEvent.click(col1Header);
        expect(await screen.findByText("🔼")).toBeInTheDocument();
        fireEvent.click(col1Header);
        expect(await screen.findByText("🔽")).toBeInTheDocument();
    });
    test("renders a table and tests button click and sorting", async () => {
        render(<OurTable columns={columns} data={threeRows} />);

        // Check that the button is rendered and clickable
        const button = await screen.findByTestId("testId-cell-row-0-col-Click-button");
        fireEvent.click(button);
        await waitFor(() => expect(clickMeCallback).toBeCalledTimes(1));

        // Test sorting on column headers
        const col1Header = screen.getByTestId("testid-header-col1");
        fireEvent.click(col1Header);
        expect(await screen.findByText("🔼")).toBeInTheDocument();
        fireEvent.click(col1Header);
        expect(await screen.findByText("🔽")).toBeInTheDocument();
    });

    test("pagination visibility based on row count", async () => {
        // Test when there is no data
        render(<OurTable columns={columns} data={[]} />);
        expect(screen.queryByTestId("testid-prev-page-button")).toBeNull();
        expect(screen.queryByTestId("testid-next-page-button")).toBeNull();

        // Test when rows are less than or equal to the page size
        render(<OurTable columns={columns} data={threeRows} />);
        expect(screen.queryByTestId("testid-prev-page-button")).toBeNull();
        expect(screen.queryByTestId("testid-next-page-button")).toBeNull();

        render(<OurTable columns={columns} data={tenRows} />);
        expect(screen.queryByTestId("testid-prev-page-button")).toBeNull();
        expect(screen.queryByTestId("testid-next-page-button")).toBeNull();

        // Test when rows exceed the page size
        render(<OurTable columns={columns} data={elevenRows} />);
        expect(await screen.findByTestId("testid-prev-page-button")).toBeInTheDocument();
        expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
    });

    test("renders a table with 100 rows and tests moving forward one page", async () => {
        render(
            <OurTable columns={columns} data={hundredRows} />
        );

        expect(await screen.findByTestId("testid-forward-one-page-button")).toBeInTheDocument();
        const forwardOneButton = screen.getByTestId("testid-forward-one-page-button");
        fireEvent.click(forwardOneButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");
        expect(await screen.findByTestId("testid-last-page-button")).toContainHTML("10");
        const lastButton = screen.getByTestId("testid-last-page-button");
        fireEvent.click(lastButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("10");
        var tester = true;
        try {
            await screen.findByTestId("testid-forward-one-page-button");
            tester = false;
        } catch(e) { }
        expect(tester).toBe(true);
        expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("9");
        const backOneButton = screen.getByTestId("testid-back-one-page-button");
        fireEvent.click(backOneButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("9");
        expect(await screen.findByTestId("testid-forward-one-page-button")).toContainHTML("10");
        fireEvent.click(await screen.findByTestId("testid-forward-one-page-button"));
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("10");
    });

    test("renders a table with 100 rows and moving back three pages", async () => {
        render(
            <OurTable columns={columns} data={hundredRows} />
        );

        expect(await screen.findByTestId("testid-forward-two-page-button")).toBeInTheDocument();
        const forwardThreeButton = screen.getByTestId("testid-forward-two-page-button");
        fireEvent.click(forwardThreeButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");
        expect(await screen.findByTestId("testid-forward-one-page-button")).toContainHTML("4");
        const forwardOneButton = screen.getByTestId("testid-forward-one-page-button");
        fireEvent.click(forwardOneButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("4");
        fireEvent.click(forwardOneButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
        expect(await screen.findByTestId("testid-back-three-page-button")).toContainHTML("2");
        const backThreeButton = screen.getByTestId("testid-back-three-page-button");
        fireEvent.click(backThreeButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");
    });

    test("renders a table with 100 rows and tests moving forward three pages", async () => {
        render(
            <OurTable columns={columns} data={hundredRows} />
        );

        expect(await screen.findByTestId("testid-last-page-button")).toBeInTheDocument();
        const lastButton = screen.getByTestId("testid-last-page-button");
        fireEvent.click(lastButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("10");
        expect(await screen.findByTestId("testid-back-two-page-button")).toContainHTML("8");
        const backTwoButton = screen.getByTestId("testid-back-two-page-button");
        fireEvent.click(backTwoButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("8");
        expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("7");
        fireEvent.click(backTwoButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("6");
        expect(await screen.findByTestId("testid-forward-three-page-button")).toContainHTML("9");
        var tester = true;
        try {
            await screen.findByTestId("testid-right-ellipsis");
            tester = false;
        } catch(e) { }
        expect(tester).toBe(true);
        const forwardThreeButton = screen.getByTestId("testid-forward-three-page-button");
        fireEvent.click(forwardThreeButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("9");
    });
    test("renders a table with 100 rows and tests the first page", async () => {
        render(
            <OurTable columns={columns} data={hundredRows} />
        );
        const nextButton = screen.getByTestId("testid-next-page-button");
        fireEvent.click(nextButton);
        const prevButton = screen.getByTestId("testid-prev-page-button");
        fireEvent.click(prevButton);

        expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
        var tester = true;
        try {
            await screen.findByTestId("testid-left-ellipses");
            tester = false;
        } catch(e) { }
        try {
            await screen.findByTestId("testid-back-three-page-button");
            tester = false;
        } catch(e) { }
        try {
            await screen.findByTestId("testid-back-two-page-button");
            tester = false;
        } catch(e) { }
        try {
            await screen.findByTestId("testid-back-one-page-button");
            tester = false;
        } catch(e) { }
        expect(tester).toBe(true);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("1");
        expect(await screen.findByTestId("testid-forward-one-page-button")).toContainHTML("2");
        expect(await screen.findByTestId("testid-forward-two-page-button")).toContainHTML("3");
        try {
            expect(await screen.findByTestId("testid-forward-three-page-button")).toBeInTheDocument();
            tester = false;
        } catch(e) { }
        expect(tester).toBe(true);
        expect(await screen.findByTestId("testid-right-ellipsis")).toBeInTheDocument();
        expect(await screen.findByTestId("testid-last-page-button")).toContainHTML("10");
        for(let i = 0; i < 10; i++) {
            expect(await screen.findByText(`Hello ${i}`)).toBeInTheDocument();
        }
    }, 10000);
    test("pagination navigation for multiple pages", async () => {
        render(<OurTable columns={columns} data={thirtyRows} />);

        const nextButton = screen.getByTestId("testid-next-page-button");
        const prevButton = screen.getByTestId("testid-prev-page-button");

        // Page 1
        expect(await screen.findByText("Hello 0")).toBeInTheDocument();
        fireEvent.click(nextButton); // Move to Page 2
        expect(await screen.findByText("Hello 10")).toBeInTheDocument();
        expect(prevButton).toHaveAttribute("disabled");

        fireEvent.click(nextButton); // Move to Page 3
        expect(await screen.findByText("Hello 20")).toBeInTheDocument();
        expect(nextButton).not.toHaveAttribute("disabled");
    });

    test("handles first and last page navigation", async () => {
        render(<OurTable columns={columns} data={hundredRows} />);

        const lastButton = await screen.findByTestId("testid-last-page-button");
        fireEvent.click(lastButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("10");

        const firstButton = await screen.findByTestId("testid-first-page-button");
        fireEvent.click(firstButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("1");
    });

    test("renders a table with 100 rows and tests right-ellipsis", async () => {
        render(
            <OurTable columns={columns} data={hundredRows} />
        );

        expect(await screen.findByTestId("testid-last-page-button")).toBeInTheDocument();
        const lastButton = screen.getByTestId("testid-last-page-button");
        fireEvent.click(lastButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("10");
        expect(await screen.findByTestId("testid-back-two-page-button")).toContainHTML("8");
        const backTwoButton = screen.getByTestId("testid-back-two-page-button");
        fireEvent.click(backTwoButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("8");
        expect(await screen.findByTestId("testid-back-two-page-button")).toContainHTML("6");
        fireEvent.click(backTwoButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("6");
        var tester = true;
        try {
            await screen.findByTestId("testid-right-ellipsis");
            tester = false;
        } catch(e) { }
        expect(tester).toBe(true);
        expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("5");
        const backOneButton = screen.getByTestId("testid-back-one-page-button");
        fireEvent.click(backOneButton);
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
        expect(await screen.findByTestId("testid-right-ellipsis")).toBeInTheDocument();
    });

    test("tests various page jumps with forward and backward buttons", async () => {
        render(<OurTable columns={columns} data={hundredRows} />);

        const forwardTwoButton = await screen.findByTestId("testid-forward-two-page-button");
        fireEvent.click(forwardTwoButton); // Jump to Page 3
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");

        const backTwoButton = screen.getByTestId("testid-back-two-page-button");
        fireEvent.click(backTwoButton); // Jump back to Page 1
        expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("1");
    });
});
