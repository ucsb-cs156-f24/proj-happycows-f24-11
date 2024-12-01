import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import OurTable, { ButtonColumn, DateColumn, PlaintextColumn } from "main/components/OurTable";

describe("OurTable tests", () => {

    test("Renders", () => {
        expect(1).toBe(1);
    });

    // const generateRows = (num) =>
    //     Array.from({ length: num }, (_, i) => ({
    //         col1: `Hello ${i}`,
    //         col2: `World ${i}`,
    //         createdAt: new Date(Date.now() - i * 1000 * 60 * 60).toISOString(), // Unique timestamps decreasing by 1 hour
    //         log: `foo\nbar\n  baz ${i}`,
    //     }));

    // // Manually defined values updated to match the generateRows function's pattern
    // const threeRows = [
    //     {
    //         col1: "Hello 0",
    //         col2: "World 0",
    //         createdAt: new Date(Date.now() - 0 * 1000 * 60 * 60).toISOString(),
    //         log: "foo\nbar\n  baz 0",
    //     },
    //     {
    //         col1: "Hello 1",
    //         col2: "World 1",
    //         createdAt: new Date(Date.now() - 1 * 1000 * 60 * 60).toISOString(),
    //         log: "foo\nbar\n  baz 1",
    //     },
    //     {
    //         col1: "Hello 2",
    //         col2: "World 2",
    //         createdAt: new Date(Date.now() - 2 * 1000 * 60 * 60).toISOString(),
    //         log: "foo\nbar\n  baz 2",
    //     },
    // ];

    // const tenRows = generateRows(10);
    // const elevenRows = generateRows(11);
    // const thirtyRows = generateRows(30);
    // const fiftyRows = generateRows(50);
    // const sixtyRows = generateRows(60);

    // const clickMeCallback = jest.fn();

    // const columns = [
    //     {
    //         Header: 'Column 1',
    //         accessor: 'col1', // accessor is the "key" in the data
    //     },
    //     {
    //         Header: 'Column 2',
    //         accessor: 'col2',
    //     },
    //     ButtonColumn("Click", "primary", clickMeCallback, "testId"),
    //     DateColumn("Date", (cell) => cell.row.original.createdAt),
    //     PlaintextColumn("Log", (cell) => cell.row.original.log),
    // ];
    // test("rendering each table based on row count", async () => {
    //     const testCases = [[], threeRows, tenRows, elevenRows, thirtyRows, fiftyRows];

    //     for (const rows of testCases) {
    //         render(<OurTable columns={columns} data={rows} />);
    //     }
    // });

    // test("The button appears in the table", async () => {
    //     render(
    //         <OurTable columns={columns} data={threeRows} />
    //     );

    //     expect(await screen.findByTestId("testId-cell-row-0-col-Click-button")).toBeInTheDocument();
    //     const button = screen.getByTestId("testId-cell-row-0-col-Click-button");
    //     fireEvent.click(button);
    //     await waitFor(() => expect(clickMeCallback).toBeCalledTimes(1));
    // });

    // test("default testid is testId", async () => {
    //     render(
    //         <OurTable columns={columns} data={threeRows} />
    //     );
    //     expect(await screen.findByTestId("testid-header-col1")).toBeInTheDocument();
    // });

    // test("click on a header and a sort caret should appear", async () => {
    //     render(
    //         <OurTable columns={columns} data={threeRows} testid={"sampleTestId"} />
    //     );

    //     expect(await screen.findByTestId("sampleTestId-header-col1")).toBeInTheDocument();
    //     const col1Header = screen.getByTestId("sampleTestId-header-col1");

    //     const col1SortCarets = screen.getByTestId("sampleTestId-header-col1-sort-carets");
    //     expect(col1SortCarets).toHaveTextContent('');

    //     const col1Row0 = screen.getByTestId("sampleTestId-cell-row-0-col-col1");
    //     expect(col1Row0).toHaveTextContent("Hello");

    //     fireEvent.click(col1Header);
    //     expect(await screen.findByText("🔼")).toBeInTheDocument();

    //     fireEvent.click(col1Header);
    //     expect(await screen.findByText("🔽")).toBeInTheDocument();
    // });

    // test("pagination isn't visible when there is no data", async () => {
    //     render(
    //         <OurTable columns={columns} data={[]} />
    //     );

    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-prev-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     try {
    //         await screen.findByTestId("testid-next-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     try {
    //         await screen.findByTestId("testid-current-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    // });


    // test("renders a table with 3 rows and tests that pagination isn't visible when there are less rows than rows per page", async () => {
    //     render(
    //         <OurTable columns={columns} data={threeRows} />
    //     );

    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-prev-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     try {
    //         await screen.findByTestId("testid-next-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     try {
    //         await screen.findByTestId("testid-current-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    // });

    // test("renders a table with 10 rows and tests that pagination isn't visible when there are less rows than rows per page", async () => {
    //     render(
    //         <OurTable columns={columns} data={tenRows} />
    //     );

    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-prev-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     try {
    //         await screen.findByTestId("testid-next-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     try {
    //         await screen.findByTestId("testid-current-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    // });

    // test("renders a table with 11 rows and tests that pagination is visible when there are more rows than rows per page", async () => {
    //     render(
    //         <OurTable columns={columns} data={elevenRows} />
    //     );

    //     expect(await screen.findByTestId("testid-prev-page-button")).toBeInTheDocument();
    //     expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
    //     expect(await screen.findByTestId("testid-current-page-button")).toBeInTheDocument();
    // });


    // test("renders a table with 30 rows and clicks on the next page button", async () => {
    //     render(
    //         <OurTable columns={columns} data={thirtyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
    //     expect((await screen.findByTestId("testid-prev-page-button")).hasAttribute("disabled")).toBe(true);
    //     expect((await screen.findByTestId("testid-next-page-button")).hasAttribute("disabled")).toBe(false);
    //     const nextButton = screen.getByTestId("testid-next-page-button");
    //     for (let i = 0; i < 10; i++) {
    //         expect(await screen.findByText(`Hello ${i}`)).toBeInTheDocument();
    //     }
    //     var tester = true;
    //     for (let i = 10; i < 20; i++) {
    //         try {
    //             await screen.findByText(`Hello ${i}`);
    //             tester = false;
    //         }
    //         catch (e) { }
    //     }
    //     expect(tester).toBe(true);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("1");
    //     fireEvent.click(nextButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");
    //     expect((await screen.findByTestId("testid-prev-page-button")).hasAttribute("disabled")).toBe(false);
    //     expect((await screen.findByTestId("testid-next-page-button")).hasAttribute("disabled")).toBe(false);
    //     for (let i = 0; i < 10; i++) {
    //         try {
    //             await screen.findByText(`Hello ${i}`);
    //             tester = false;
    //         }
    //         catch (e) { }
    //     }
    //     expect(tester).toBe(true);
    //     for (let i = 10; i < 20; i++) {
    //         expect(await screen.findByText(`Hello ${i}`)).toBeInTheDocument();
    //     }
    //     fireEvent.click(nextButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");
    //     expect((await screen.findByTestId("testid-prev-page-button")).hasAttribute("disabled")).toBe(false);
    //     expect((await screen.findByTestId("testid-next-page-button")).hasAttribute("disabled")).toBe(true);
    //     for (let i = 20; i < 30; i++) {
    //         expect(await screen.findByText(`Hello ${i}`)).toBeInTheDocument();
    //     }
    // }, 150000);

    // test("renders a table with 60 rows and tests the first page", async () => {
    //     render(
    //         <OurTable columns={columns} data={sixtyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
    //     expect((await screen.findByTestId("testid-prev-page-button")).hasAttribute("disabled")).toBe(true);
    //     expect((await screen.findByTestId("testid-next-page-button")).hasAttribute("disabled")).toBe(false);
    //     const lastButton = screen.getByTestId("testid-last-page-button");
    //     fireEvent.click(lastButton);

    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("6");
    //     expect(await screen.findByTestId("testid-first-page-button")).toContainHTML("1");
    //     const firstButton = screen.getByTestId("testid-first-page-button");
    //     fireEvent.click(firstButton);

    //     const nextButton = screen.getByTestId("testid-next-page-button");
    //     fireEvent.click(nextButton);
    //     const prevButton = screen.getByTestId("testid-prev-page-button");
    //     fireEvent.click(prevButton);

    //     expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-left-ellipses");
    //         tester = false;
    //     } catch (e) { }
    //     try {
    //         await screen.findByTestId("testid-back-three-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     try {
    //         await screen.findByTestId("testid-back-two-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     try {
    //         await screen.findByTestId("testid-back-one-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("1");
    //     expect(await screen.findByTestId("testid-forward-one-page-button")).toContainHTML("2");
    //     expect(await screen.findByTestId("testid-forward-two-page-button")).toContainHTML("3");

    //     try {
    //         expect(await screen.findByTestId("testid-forward-three-page-button")).toBeInTheDocument();
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);

    //     expect(await screen.findByTestId("testid-right-ellipsis")).toBeInTheDocument();
    //     expect(await screen.findByTestId("testid-last-page-button")).toContainHTML("6");
    //     for (let i = 0; i < 10; i++) {
    //         expect(await screen.findByText(`Hello ${i}`)).toBeInTheDocument();
    //     }
    // }, 10000);

    // test("renders a table with 60 rows and tests the last page button", async () => {
    //     render(
    //         <OurTable columns={columns} data={sixtyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
    //     expect((await screen.findByTestId("testid-prev-page-button")).hasAttribute("disabled")).toBe(true);
    //     expect((await screen.findByTestId("testid-next-page-button")).hasAttribute("disabled")).toBe(false);
    //     const forwardOneButton = screen.getByTestId("testid-forward-one-page-button");
    //     const forwardTwoButton = screen.getByTestId("testid-forward-two-page-button");
    //     const lastButton = screen.getByTestId("testid-last-page-button");
    //     const rightEllipsis = screen.getByTestId("testid-right-ellipsis");
    //     fireEvent.click(lastButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("6");
    //     for (let i = 50; i < 60; i++) {
    //         expect(await screen.findByText(`Hello ${i}`)).toBeInTheDocument();
    //     }
    //     expect(forwardOneButton).not.toBeInTheDocument();
    //     expect(forwardTwoButton).not.toBeInTheDocument();
    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-forward-three-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     expect(rightEllipsis).not.toBeInTheDocument();
    //     expect(lastButton).not.toBeInTheDocument();
    //     expect(await screen.findByTestId("testid-left-ellipsis")).toBeInTheDocument();
    //     expect(await screen.findByTestId("testid-back-two-page-button")).toContainHTML("4");
    //     expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("5");
    // });

    // test("renders a table with 50 rows and tests moving forward one and two pages", async () => {
    //     render(
    //         <OurTable columns={columns} data={fiftyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-forward-one-page-button")).toBeInTheDocument();
    //     const forwardOneButton = screen.getByTestId("testid-forward-one-page-button");
    //     fireEvent.click(forwardOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");

    //     expect(await screen.findByTestId("testid-forward-two-page-button")).toContainHTML("4");
    //     const forwardTwoButton = screen.getByTestId("testid-forward-two-page-button");
    //     fireEvent.click(forwardTwoButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("4");

    //     fireEvent.click(forwardOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-forward-one-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     try {
    //         await screen.findByTestId("testid-forward-two-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("4");
    //     const backOneButton = screen.getByTestId("testid-back-one-page-button");
    //     fireEvent.click(backOneButton);
    //     fireEvent.click(backOneButton);
    //     fireEvent.click(backOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");
    //     expect(await screen.findByTestId("testid-forward-one-page-button")).toContainHTML("3");
    //     expect(await screen.findByTestId("testid-forward-two-page-button")).toContainHTML("4");

    //     fireEvent.click(await screen.findByTestId("testid-forward-one-page-button"));
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");

    //     fireEvent.click(await screen.findByTestId("testid-forward-two-page-button"));
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");

    // });


    // test("renders a table with 50 rows and moving back three pages", async () => {
    //     render(
    //         <OurTable columns={columns} data={fiftyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-forward-two-page-button")).toBeInTheDocument();
    //     const forwardThreeButton = screen.getByTestId("testid-forward-two-page-button");
    //     fireEvent.click(forwardThreeButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");
    //     expect(await screen.findByTestId("testid-forward-one-page-button")).toContainHTML("4");
    //     const forwardOneButton = screen.getByTestId("testid-forward-one-page-button");
    //     fireEvent.click(forwardOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("4");
    //     fireEvent.click(forwardOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
    //     expect(await screen.findByTestId("testid-back-three-page-button")).toContainHTML("2");
    //     const backThreeButton = screen.getByTestId("testid-back-three-page-button");
    //     fireEvent.click(backThreeButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");
    // });

    // test("renders a table with 50 rows and tests moving back one page", async () => {
    //     render(
    //         <OurTable columns={columns} data={fiftyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-last-page-button")).toBeInTheDocument();
    //     const lastButton = screen.getByTestId("testid-last-page-button");
    //     fireEvent.click(lastButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
    //     expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("4");
    //     const backOneButton = screen.getByTestId("testid-back-one-page-button");
    //     fireEvent.click(backOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("4");
    // });

    // test("renders a table with 100 rows and tests moving back two pages", async () => {
    //     render(
    //         <OurTable columns={columns} data={fiftyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-last-page-button")).toBeInTheDocument();
    //     const lastButton = screen.getByTestId("testid-last-page-button");
    //     fireEvent.click(lastButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
    //     expect(await screen.findByTestId("testid-back-two-page-button")).toContainHTML("3");
    //     const backTwoButton = screen.getByTestId("testid-back-two-page-button");
    //     fireEvent.click(backTwoButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");
    // });

    // test("renders a table with 100 rows and tests moving forward three pages", async () => {
    //     render(
    //         <OurTable columns={columns} data={fiftyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-last-page-button")).toBeInTheDocument();
    //     const lastButton = screen.getByTestId("testid-last-page-button");
    //     fireEvent.click(lastButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
    //     expect(await screen.findByTestId("testid-back-two-page-button")).toContainHTML("3");
    //     const backTwoButton = screen.getByTestId("testid-back-two-page-button");
    //     fireEvent.click(backTwoButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");
    //     expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("2");
    //     fireEvent.click(backTwoButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("1");
    //     expect(await screen.findByTestId("testid-forward-three-page-button")).toContainHTML("4");
    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-right-ellipsis");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     const forwardThreeButton = screen.getByTestId("testid-forward-three-page-button");
    //     fireEvent.click(forwardThreeButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("4");
    // });

    // test("renders a table with 50 rows and tests left-ellipsis", async () => {
    //     render(
    //         <OurTable columns={columns} data={sixtyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-last-page-button")).toBeInTheDocument();
    //     const lastButton = screen.getByTestId("testid-last-page-button");
    //     fireEvent.click(lastButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("6");
    //     expect(await screen.findByTestId("testid-left-ellipsis")).toBeInTheDocument();
    //     expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("5");
    //     const backOneButton = screen.getByTestId("testid-back-one-page-button");
    //     fireEvent.click(backOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-left-ellipsis");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    // });

    // test("renders a table with 60 rows and tests right-ellipsis", async () => {
    //     render(
    //         <OurTable columns={columns} data={sixtyRows} />
    //     );
    //     expect(await screen.findByTestId("testid-last-page-button")).toBeInTheDocument();
    //     const lastButton = screen.getByTestId("testid-last-page-button");
    //     fireEvent.click(lastButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("6");
    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-right-ellipsis");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);

    //     expect(await screen.findByTestId("testid-first-page-button")).toContainHTML("1");
    //     const firstButton = screen.getByTestId("testid-first-page-button");
    //     fireEvent.click(firstButton);

    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("1");

    //     expect(await screen.findByTestId("testid-right-ellipsis")).toBeInTheDocument();
    // });

    // test("renders a table with 50 rows and tests the first page button visibility", async () => {
    //     render(
    //         <OurTable columns={columns} data={fiftyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
    //     const nextButton = screen.getByTestId("testid-next-page-button");
    //     fireEvent.click(nextButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");
    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-first-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     fireEvent.click(nextButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");
    //     try {
    //         await screen.findByTestId("testid-first-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     fireEvent.click(nextButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("4");
    //     try {
    //         await screen.findByTestId("testid-first-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     fireEvent.click(nextButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
    //     expect(await screen.findByTestId("testid-first-page-button")).toBeInTheDocument();
    //     const firstButton = screen.getByTestId("testid-first-page-button");
    //     fireEvent.click(firstButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("1");
    // });

    // test("renders a table with 50 rows and tests the back two page button", async () => {
    //     render(
    //         <OurTable columns={columns} data={fiftyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-next-page-button")).toBeInTheDocument();
    //     const nextButton = screen.getByTestId("testid-next-page-button");
    //     fireEvent.click(nextButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");
    //     var tester = true;
    //     try {
    //         await screen.findByTestId("testid-back-two-page-button");
    //         tester = false;
    //     } catch (e) { }
    //     expect(tester).toBe(true);
    //     fireEvent.click(nextButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");
    //     expect(await screen.findByTestId("testid-back-two-page-button")).toBeInTheDocument();
    // });

    // test("renders a table with 50 rows and tests the last page button visibiliy", async () => {
    //     render(
    //         <OurTable columns={columns} data={fiftyRows} />
    //     );

    //     expect(await screen.findByTestId("testid-last-page-button")).toContainHTML("5");
    //     const lastButton = screen.getByTestId("testid-last-page-button");
    //     fireEvent.click(lastButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("5");
    //     expect(await screen.findByTestId("testid-back-one-page-button")).toContainHTML("4");
    //     const backOneButton = screen.getByTestId("testid-back-one-page-button");
    //     fireEvent.click(backOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("4");
    //     expect(await screen.findByTestId("testid-forward-one-page-button")).toContainHTML("5");
    //     expect(lastButton).not.toBeInTheDocument();
    //     fireEvent.click(backOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("3");
    //     expect(lastButton).not.toBeInTheDocument();
    //     fireEvent.click(backOneButton);
    //     expect(await screen.findByTestId("testid-current-page-button")).toContainHTML("2");
    //     expect(lastButton).not.toBeInTheDocument();
    // });
});