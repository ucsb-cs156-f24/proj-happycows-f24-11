
import React from 'react';
import { apiCurrentUserFixtures } from "fixtures/currentUserFixtures";
import { systemInfoFixtures } from "fixtures/systemInfoFixtures";
import { coursesFixtures } from 'fixtures/coursesFixtures';
import { rest } from "msw";

import CoursesEditPage from 'main/pages/CoursesEditPage';

export default {
    title: 'pages/Courses/CoursesEditPage',
    component: CoursesEditPage
};

const Template = () => <CoursesEditPage storybook={true}/>;

export const Default = Template.bind({});
Default.parameters = {
    msw: [
        rest.get('/api/currentUser', (_req, res, ctx) => {
            return res( ctx.json(apiCurrentUserFixtures.adminUser));
        }),
        rest.get('/api/systemInfo', (_req, res, ctx) => {
            return res(ctx.json(systemInfoFixtures.showingNeither));
        }),
        rest.get('/api/courses', (_req, res, ctx) => {
            return res(ctx.json(coursesFixtures.threeCourses[0]));
        }),
        rest.put('/api/courses', async (req, res, ctx) => {
            var reqBody = await req.text();
            window.alert("PUT: " + req.url + " and body: " + reqBody);
            return res(ctx.status(200),ctx.json({}));
        }),
    ],
}


