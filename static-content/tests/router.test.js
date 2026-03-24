import {Router} from "../routers/router.js";
import {a, p} from "../js/dom/domTags.js";
import {LogError} from "../js/errorUtils.js";
import {createState} from "../js/compLib.js";

const assert = chai.assert;

/**
 * Test suite for the Router module.
 * Ensures the Router behaves correctly in various routing scenarios.
 */
describe('Router', () => {

    /**
     * Basic tests to ensure the Router factory is defined and returns instances correctly.
     */
    it('should be a function', () => {
        assert.typeOf(Router, 'function');
    });

    it('should return a function when called', () => {
        assert.typeOf(Router(), 'function');
    });

    it('should return a new instance each time', () => {
        const router1 = Router();
        const router2 = Router();
        assert.notStrictEqual(router1, router2);
    });

    /**
     * Tests related to general routing behavior.
     */
    describe('Routing logic', () => {

        /**
         * Tests for static path matching and default handler behavior.
         */
        describe('Basic path match', () => {

            it('routes correctly for a valid static path', async () => {
                const state = createState("/");
                const router = Router();

                router.addHandler('/', async () => p("Static match"));

                const element = await router(state);

                assert.isDefined(element);
                assert.strictEqual(element.tagName, 'P');
                assert.strictEqual(state.currentPath, '');
            });

            it('throws error for unmatched path without default handler', async () => {
                const state = createState('/unknown');

                const router = Router();
                router.addHandler('/', async () => p("Main"));

                try {
                    await router(state);
                    assert.fail("Should have thrown LogError");
                } catch (e) {
                    assert.instanceOf(e, LogError);
                }
            });

            it('uses default handler if path is unmatched', async () => {
                const state = createState('/fallback');

                const router = Router();
                router.addHandler('/', async () => p("Main"));
                router.addDefaultHandler(async () => a("Fallback"));

                const element = await router(state);

                assert.isDefined(element);
                assert.strictEqual(element.tagName, 'A');
                assert.strictEqual(state.currentPath, '/fallback');
            });
        });

        /**
         * Tests for matching nested paths and updating remaining path correctly.
         */
        describe('Nested path matching', () => {
            it('matches and trims prefix correctly', async () => {
                const state = createState('/section/item/extra');
                const router = Router();

                router.addHandler('/section/item', async () => p("Nested"));

                const element = await router(state);

                assert.isDefined(element);
                assert.strictEqual(element.tagName, 'P');
                assert.strictEqual(state.currentPath, '/extra');
            });

            it('fails when nested path is incorrect', async () => {
                const state = createState('/section/itm');

                const router = Router();
                router.addHandler('/section/item', async () => p("Nested"));

                try {
                    await router(state);
                    assert.fail("Should have thrown LogError");
                } catch (e) {
                    assert.instanceOf(e, LogError);
                }
            });
        });

        /**
         * Tests for dynamic path parameters (e.g. /users/:userId).
         */
        describe('Dynamic parameter matching', () => {
            it('extracts path parameter and updates currentPath', async () => {
                const state = createState('/users/123/bookings');

                const router = Router();
                router.addHandler('/users/:userId', async () => p("User page"));

                const element = await router(state);

                assert.isDefined(element);
                assert.strictEqual(element.tagName, 'P');
                assert.strictEqual(state.params.userId, '123');
                assert.strictEqual(state.currentPath, '/bookings');
            });

            it('fails when dynamic path lacks value', async () => {
                const state = createState('/users/');

                const router = Router();
                router.addHandler('/users/:userId', async () => p("User page"));

                try {
                    await router(state);
                    assert.fail("Should have thrown LogError");
                } catch (e) {
                    assert.instanceOf(e, LogError);
                }
            });
        });
    });
});
